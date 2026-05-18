package com.challenge.outsera.infrastructure.studio.persistence;

import com.challenge.outsera.domain.studio.StudioEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "studio")
@Table(name = "studio")
public class StudioJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public static StudioJpaEntity toJpaEntity(StudioEntity entity) {
        StudioJpaEntity jpaEntity = new StudioJpaEntity();

        jpaEntity.id = entity.id();
        jpaEntity.name = entity.name();

        return jpaEntity;
    }

    public StudioEntity toEntity() {
        return new StudioEntity(id, name);
    }
}
