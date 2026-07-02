package com.beanteacher.deepfine.site.repository;

import com.beanteacher.deepfine.site.domain.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Long> {
}
