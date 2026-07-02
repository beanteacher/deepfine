package com.beanteacher.deepfine.zone.repository;

import com.beanteacher.deepfine.zone.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    List<Zone> findBySiteId(Long siteId);
}
