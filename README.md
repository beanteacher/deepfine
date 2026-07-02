# deepfine — 재고 관리 시스템 (Inventory Management System)

DeepFine의 공간 지능(Spatial Intelligence) 플랫폼을 위한 재고 관리 백엔드 MVP.
현장(Site) · 구역(Zone) · 품목(Item) 단위로 재고를 추적하며, 입고 · 출고 · 재고 조정을 안전하게 처리합니다.

---

## 주요 기능

- **입고 (Inbound)** — 품목을 구역에 입고. 신규 품목·재고 행이 없으면 자동 생성
- **출고 (Outbound)** — 재고 수량 차감. 잔량 부족 시 예외 반환
- **재고 조정 (Adjustment)** — 실사 후 수량을 목표값으로 덮어쓰기
- **현장 · 구역 관리** — 물류 / 제조 / 조선 등 SiteType 기반 현장 등록 및 구역 분류
- **동시성 제어** — Pessimistic Lock으로 동시 재고 수정 충돌 방지

---

## 기술 스택

| 구분 | 내용 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Build | Gradle |
| Database | PostgreSQL 16 |
| ORM | Spring Data JPA (Hibernate) |
| Infra | Docker Compose |

---

## 아키텍처 — 확장성 · 유지보수성 설계

### 1. Strategy Pattern — 재고 이동 유형 분기

`StockMovementStrategy` 인터페이스를 구현한 전략 클래스가 각 이동 유형을 독립적으로 처리합니다.
새로운 이동 유형 추가 시 **기존 코드 수정 없이** 새 구현체만 추가하면 됩니다 (OCP).

```
StockMovementStrategy (interface)
├── InboundMovementStrategy   → inventory.increaseQuantity()
├── OutboundMovementStrategy  → inventory.decreaseQuantity()
└── AdjustmentMovementStrategy → inventory.adjustQuantity()
```

### 2. Factory Pattern — 전략 자동 등록 (DIP)

`StockMovementStrategyFactory`는 Spring이 주입한 `List<StockMovementStrategy>`를
`Map<MovementType, Strategy>`으로 변환해 보관합니다.
새 전략을 `@Component`로 등록하면 팩토리가 **코드 수정 없이 자동으로 인식**합니다.

```java
// 새 이동 유형 추가 예시 — 팩토리 수정 불필요
@Component
public class ReturnMovementStrategy implements StockMovementStrategy {
    public MovementType getMovementType() { return MovementType.RETURN; }
    public void process(Inventory inventory, int quantity) { inventory.increaseQuantity(quantity); }
}
```

### 3. Factory Method Pattern — 도메인 객체 생성 캡슐화

Entity 생성 시 `static create()` 팩토리 메서드로 생성 로직과 검증을 캡슐화합니다.
외부에서 `new` 직접 호출을 방지하고 불변 불변성을 보장합니다.

```java
Item item = Item.create("철근 H형강");
Inventory inventory = Inventory.create(item, zone);
```

### 4. 레이어 분리 + 단방향 의존 (SRP · DIP)

```
Controller  →  Service  →  Repository  →  Domain(Entity)
```

- Controller: HTTP 수신/응답만 담당
- Service: 비즈니스 로직 + 트랜잭션 경계
- Domain: 도메인 메서드 캡슐화 (increaseQuantity, decreaseQuantity 등)
- Entity를 API 응답으로 직접 노출하지 않고 DTO로 변환

### 5. Pessimistic Lock — 동시성 제어

동시 요청으로 동일 재고 행이 수정될 때 데이터 정합성을 보장합니다.

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT i FROM Inventory i WHERE i.item.id = :itemId AND i.zone.id = :zoneId")
Optional<Inventory> findByItemIdAndZoneIdWithLock(Long itemId, Long zoneId);
```

| 케이스 | 처리 방식 |
|--------|-----------|
| 기존 재고 동시 수정 | PESSIMISTIC_WRITE → 선점 스레드가 완료 후 해제 |
| 신규 품목/재고 동시 생성 | UniqueConstraint 위반 → 409 Conflict 반환 |

---

## 도메인 모델

```
Site (현장)
├── SiteType: LOGISTICS / MANUFACTURING / SHIPBUILDING / MRO / PUBLIC
└── Zone (구역) 1..N
    └── Inventory (품목별 재고) = Item × Zone × quantity

StockMovement (이동 이력)
└── item, zone, movementType, quantity, createdAt
```

---

## API 엔드포인트

### 재고 이동

| Method | URI | 설명 |
|--------|-----|------|
| `POST` | `/api/v1/stocks/inbound` | 입고 |
| `POST` | `/api/v1/stocks/outbound` | 출고 |
| `POST` | `/api/v1/stocks/adjustment` | 재고 조정 |

### 현장 · 구역 · 품목

| Method | URI | 설명 |
|--------|-----|------|
| `POST` | `/api/v1/sites` | 현장 등록 |
| `GET` | `/api/v1/sites` | 현장 목록 |
| `GET` | `/api/v1/sites/{id}` | 현장 단건 조회 |
| `POST` | `/api/v1/zones` | 구역 등록 |
| `GET` | `/api/v1/zones` | 구역 목록 |
| `GET` | `/api/v1/items` | 품목 목록 (구역별 재고 포함) |
| `GET` | `/api/v1/items/{id}` | 품목 단건 조회 |

### 요청/응답 예시

**입고**
```json
POST /api/v1/stocks/inbound
{
  "itemName": "철근 H형강",
  "zoneId": 1,
  "quantity": 100
}
```

**응답**
```json
{
  "success": true,
  "data": {
    "itemId": 1,
    "itemName": "철근 H형강",
    "zoneId": 1,
    "zoneName": "A-1구역",
    "siteId": 1,
    "siteName": "부산 조선소",
    "siteType": "SHIPBUILDING",
    "quantity": 100
  },
  "message": null,
  "timestamp": "2026-07-02T10:00:00"
}
```

---

## 실행 방법

### 사전 준비

- Docker Desktop
- Java 17+
- IntelliJ IDEA (권장, 내장 JDK 사용)

### 1. DB 실행

```bash
docker compose up -d
```

PostgreSQL이 `localhost:5432`에 실행됩니다.

### 2. 애플리케이션 실행

**IntelliJ IDEA**
```
DeepfineApplication.java → Run
```

**터미널**
```bash
./gradlew bootRun
```

### 3. 동작 확인

```
http://localhost:8080/actuator/health
```

---

## 패키지 구조

```
src/main/java/com/beanteacher/deepfine/
├── config/          # JPA Auditing 설정
├── dto/             # ApiResponse<T> 공통 응답 래퍼
├── exception/       # GlobalExceptionHandler, InsufficientStockException
├── item/            # 품목 (Item)
├── site/            # 현장 (Site, SiteType)
├── zone/            # 구역 (Zone)
├── inventory/       # 재고 (Inventory)
└── stock/
    ├── controller/  # StockController
    ├── domain/      # StockMovement, MovementType
    ├── dto/         # InboundRequest, OutboundRequest, AdjustmentRequest, Response
    ├── repository/  # StockMovementRepository
    ├── service/     # StockService
    └── strategy/    # StockMovementStrategy, Factory, 구현체 3종
```
