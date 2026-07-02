package com.beanteacher.deepfine.stock.dto;

import com.beanteacher.deepfine.inventory.domain.Inventory;
import com.beanteacher.deepfine.site.domain.SiteType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class StockDto {

    public record InboundRequest(
            @NotBlank(message = "품목명은 필수입니다") String itemName,
            @NotNull(message = "구역 ID는 필수입니다") Long zoneId,
            @Positive(message = "수량은 0보다 커야 합니다") int quantity
    ) {}

    public record OutboundRequest(
            @NotNull(message = "품목 ID는 필수입니다") Long itemId,
            @NotNull(message = "구역 ID는 필수입니다") Long zoneId,
            @Positive(message = "수량은 0보다 커야 합니다") int quantity
    ) {}

    public record AdjustmentRequest(
            @NotNull(message = "품목 ID는 필수입니다") Long itemId,
            @NotNull(message = "구역 ID는 필수입니다") Long zoneId,
            @Min(value = 0, message = "조정 수량은 0 이상이어야 합니다") int targetQuantity
    ) {}

    public record Response(
            Long itemId,
            String itemName,
            Long zoneId,
            String zoneName,
            Long siteId,
            String siteName,
            SiteType siteType,
            int quantity
    ) {
        public static Response from(Inventory inventory) {
            return new Response(
                    inventory.getItem().getId(),
                    inventory.getItem().getName(),
                    inventory.getZone().getId(),
                    inventory.getZone().getName(),
                    inventory.getZone().getSite().getId(),
                    inventory.getZone().getSite().getName(),
                    inventory.getZone().getSite().getType(),
                    inventory.getQuantity()
            );
        }
    }
}
