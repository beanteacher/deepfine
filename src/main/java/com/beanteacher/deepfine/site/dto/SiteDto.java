package com.beanteacher.deepfine.site.dto;

import com.beanteacher.deepfine.site.domain.Site;
import com.beanteacher.deepfine.site.domain.SiteType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SiteDto {

    public record CreateRequest(
            @NotBlank(message = "현장명은 필수입니다") String name,
            @NotNull(message = "현장 유형은 필수입니다") SiteType type,
            @NotBlank(message = "주소는 필수입니다") String address
    ) {}

    public record Response(Long id, String name, SiteType type, String address) {
        public static Response from(Site site) {
            return new Response(site.getId(), site.getName(), site.getType(), site.getAddress());
        }
    }
}
