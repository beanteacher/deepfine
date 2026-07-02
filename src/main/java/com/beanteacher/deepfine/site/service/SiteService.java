package com.beanteacher.deepfine.site.service;

import com.beanteacher.deepfine.site.domain.Site;
import com.beanteacher.deepfine.site.dto.SiteDto;
import com.beanteacher.deepfine.site.repository.SiteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SiteService {

    private final SiteRepository siteRepository;

    public SiteDto.Response create(SiteDto.CreateRequest request) {
        return SiteDto.Response.from(siteRepository.save(Site.create(request.name(), request.type(), request.address())));
    }

    @Transactional(readOnly = true)
    public List<SiteDto.Response> getAll() {
        return siteRepository.findAll().stream().map(SiteDto.Response::from).toList();
    }

    @Transactional(readOnly = true)
    public SiteDto.Response getById(Long id) {
        return siteRepository.findById(id)
                .map(SiteDto.Response::from)
                .orElseThrow(() -> new EntityNotFoundException("현장을 찾을 수 없습니다: " + id));
    }
}
