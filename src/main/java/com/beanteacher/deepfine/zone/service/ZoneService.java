package com.beanteacher.deepfine.zone.service;

import com.beanteacher.deepfine.site.domain.Site;
import com.beanteacher.deepfine.site.repository.SiteRepository;
import com.beanteacher.deepfine.zone.domain.Zone;
import com.beanteacher.deepfine.zone.dto.ZoneDto;
import com.beanteacher.deepfine.zone.repository.ZoneRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final SiteRepository siteRepository;

    public ZoneDto.Response create(ZoneDto.CreateRequest request) {
        Site site = siteRepository.findById(request.siteId())
                .orElseThrow(() -> new EntityNotFoundException("현장을 찾을 수 없습니다: " + request.siteId()));
        return ZoneDto.Response.from(zoneRepository.save(Zone.create(request.name(), site, request.description())));
    }

    @Transactional(readOnly = true)
    public List<ZoneDto.Response> getAll() {
        return zoneRepository.findAll().stream().map(ZoneDto.Response::from).toList();
    }

    @Transactional(readOnly = true)
    public ZoneDto.Response getById(Long id) {
        return zoneRepository.findById(id)
                .map(ZoneDto.Response::from)
                .orElseThrow(() -> new EntityNotFoundException("구역을 찾을 수 없습니다: " + id));
    }
}
