-- ============================================================
-- DeepFine 재고 관리 시스템 DDL
-- DB: PostgreSQL 16
-- ============================================================

CREATE TABLE sites
(
    id         BIGSERIAL    NOT NULL,
    name       VARCHAR(255) NOT NULL,
    type       VARCHAR(50)  NOT NULL, -- LOGISTICS | MANUFACTURING | SHIPBUILDING | MRO | PUBLIC
    address    VARCHAR(500) NOT NULL,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE zones
(
    id          BIGSERIAL    NOT NULL,
    name        VARCHAR(255) NOT NULL,
    site_id     BIGINT       NOT NULL,
    description VARCHAR(500),
    created_at  TIMESTAMP(6),
    updated_at  TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_zones_site FOREIGN KEY (site_id) REFERENCES sites (id)
);

CREATE TABLE items
(
    id         BIGSERIAL    NOT NULL,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT uq_items_name UNIQUE (name)
);

CREATE TABLE inventories
(
    id         BIGSERIAL NOT NULL,
    item_id    BIGINT    NOT NULL,
    zone_id    BIGINT    NOT NULL,
    quantity   INT       NOT NULL DEFAULT 0,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT uq_inventories_item_zone UNIQUE (item_id, zone_id),
    CONSTRAINT fk_inventories_item FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_inventories_zone FOREIGN KEY (zone_id) REFERENCES zones (id)
);

CREATE TABLE stock_movements
(
    id            BIGSERIAL   NOT NULL,
    item_id       BIGINT      NOT NULL,
    zone_id       BIGINT      NOT NULL,
    movement_type VARCHAR(50) NOT NULL, -- INBOUND | OUTBOUND | ADJUSTMENT
    quantity      INT         NOT NULL,
    created_at    TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_stock_movements_item FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_stock_movements_zone FOREIGN KEY (zone_id) REFERENCES zones (id)
);
