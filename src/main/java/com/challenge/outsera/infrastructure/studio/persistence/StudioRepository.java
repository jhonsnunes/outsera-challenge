package com.challenge.outsera.infrastructure.studio.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudioRepository extends JpaRepository<StudioJpaEntity, Long> {
    Optional<StudioJpaEntity> findByName(String name);
}
