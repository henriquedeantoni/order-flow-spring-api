package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
}
