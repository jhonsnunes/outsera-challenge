package com.challenge.outsera.infrastructure.producer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProducerRepository extends JpaRepository<ProducerJpaEntity, Long> {
    Optional<ProducerJpaEntity> findByName(String name);
}
