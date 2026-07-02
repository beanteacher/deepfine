package com.beanteacher.deepfine.item.dto;

import com.beanteacher.deepfine.inventory.domain.Inventory;
import com.beanteacher.deepfine.item.domain.Item;
import com.beanteacher.deepfine.site.domain.SiteType;

import java.util.List;

public class ItemDto {

    public record Response(
            Long id,
            String name,
            int totalQuantity,
            List<ZoneInventory> zoneInventories
    ) {
        public record ZoneInventory(
                Long zoneId,
                String zoneName,
                Long siteId,
                String siteName,
                SiteType siteType,
                int quantity
        ) {}

        public static Response from(Item item, List<Inventory> inventories) {
            int totalQuantity = inventories.stream().mapToInt(Inventory::getQuantity).sum();
            List<ZoneInventory> zoneInventories = inventories.stream()
                    .map(inv -> new ZoneInventory(
                            inv.getZone().getId(),
                            inv.getZone().getName(),
                            inv.getZone().getSite().getId(),
                            inv.getZone().getSite().getName(),
                            inv.getZone().getSite().getType(),
                            inv.getQuantity()
                    ))
                    .toList();
            return new Response(item.getId(), item.getName(), totalQuantity, zoneInventories);
        }
    }
}
