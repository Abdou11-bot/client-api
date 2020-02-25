package com.project.clientapi.repositories.image;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
    ImageModel findFirstByClientId(Long id);
    void deleteAllByClientIdEquals(Long id);
}
