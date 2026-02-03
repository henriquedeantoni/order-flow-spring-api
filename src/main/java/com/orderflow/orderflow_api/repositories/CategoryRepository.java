package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
