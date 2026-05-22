package com.challenge.outsera.application.provider;

import com.challenge.outsera.domain.producer.ProducerEntity;

import java.util.Optional;

public interface ProducerProvider {
    ProducerEntity save(ProducerEntity entity);

    Optional<ProducerEntity> findByName(String name);
}
