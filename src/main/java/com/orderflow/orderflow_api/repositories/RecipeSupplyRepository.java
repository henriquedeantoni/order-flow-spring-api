package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.RecipeSupply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeSupplyRepository extends JpaRepository<RecipeSupply, Long> {
}
