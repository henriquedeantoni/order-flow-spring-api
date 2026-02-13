package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.AlbumImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumImageRepository extends JpaRepository<AlbumImage, Long> {
}
