package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.SimpleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleImageRepository extends JpaRepository<SimpleImage, Long> {
}
