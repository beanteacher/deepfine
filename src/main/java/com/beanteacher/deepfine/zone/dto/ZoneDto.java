package com.beanteacher.deepfine.zone.dto;

import com.beanteacher.deepfine.zone.domain.Zone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ZoneDto {

    public record CreateRequest(
            @NotBlank(message = "구역명은 필수입니다") String name,
            @NotNull(message = "현장 ID는 필수입니다") Long siteId,
            String description
    ) {}

    public record Response(Long id, String name, Long siteId, String siteName, String description) {
        public static Response from(Zone zone) {
            return new Response(
                    zone.getId(),
                    zone.getName(),
                    zone.getSite().getId(),
                    zone.getSite().getName(),
                    zone.getDescription()
            );
        }
    }
}
