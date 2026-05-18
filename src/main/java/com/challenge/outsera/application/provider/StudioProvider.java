package com.challenge.outsera.application.provider;

import com.challenge.outsera.domain.studio.StudioEntity;

import java.util.Optional;

public interface StudioProvider {
    Optional<StudioEntity> findByName(String name);
}
