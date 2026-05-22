package com.challenge.outsera.infrastructure.studio;

import com.challenge.outsera.application.provider.StudioProvider;
import com.challenge.outsera.domain.studio.StudioEntity;
import com.challenge.outsera.infrastructure.studio.persistence.StudioJpaEntity;
import com.challenge.outsera.infrastructure.studio.persistence.StudioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StudioProviderImpl implements StudioProvider {
    private final StudioRepository studioRepository;

    @Override
    public StudioEntity save(StudioEntity entity) {
        return studioRepository
                .save(StudioJpaEntity.toJpaEntity(entity))
                .toEntity();
    }

    @Override
    public Optional<StudioEntity> findByName(String name) {
        return studioRepository
                .findByName(name)
                .map(StudioJpaEntity::toEntity);
    }
}
