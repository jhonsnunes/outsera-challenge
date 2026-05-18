package com.challenge.outsera.infrastructure.producer.persistence;

import com.challenge.outsera.domain.producer.ProducerEntity;
import com.challenge.outsera.domain.studio.StudioEntity;
import com.challenge.outsera.infrastructure.studio.persistence.StudioJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "producer")
@Table(name = "producer")
public class ProducerJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ProducerJpaEntity toJpaEntity(ProducerEntity entity) {
        ProducerJpaEntity jpaEntity = new ProducerJpaEntity();

        jpaEntity.id = entity.id();
        jpaEntity.name = entity.name();

        return jpaEntity;
    }

    public ProducerEntity toEntity() {
        return new ProducerEntity(id, name);
    }
}
