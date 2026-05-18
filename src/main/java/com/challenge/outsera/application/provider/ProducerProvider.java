package com.challenge.outsera.application.provider;

import com.challenge.outsera.domain.producer.ProducerEntity;

import java.util.Optional;

public interface ProducerProvider {
    Optional<ProducerEntity> findByName(String name);
}
