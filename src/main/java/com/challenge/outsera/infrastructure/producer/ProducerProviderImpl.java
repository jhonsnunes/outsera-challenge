package com.challenge.outsera.infrastructure.producer;

import com.challenge.outsera.application.provider.ProducerProvider;
import com.challenge.outsera.domain.producer.ProducerEntity;
import com.challenge.outsera.infrastructure.producer.persistence.ProducerJpaEntity;
import com.challenge.outsera.infrastructure.producer.persistence.ProducerRepository;
import com.challenge.outsera.infrastructure.studio.persistence.StudioJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProducerProviderImpl implements ProducerProvider {
    private final ProducerRepository producerRepository;

    @Override
    public ProducerEntity save(ProducerEntity entity) {
        return producerRepository
            .save(ProducerJpaEntity.toJpaEntity(entity))
            .toEntity();
    }

    @Override
    public Optional<ProducerEntity> findByName(String name) {
        return producerRepository
                .findByName(name)
                .map(ProducerJpaEntity::toEntity);
    }
}
