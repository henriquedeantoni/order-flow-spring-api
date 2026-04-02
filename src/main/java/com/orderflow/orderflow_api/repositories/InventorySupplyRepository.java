package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.InventorySupply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface InventorySupplyRepository extends JpaRepository<InventorySupply,Long>,  JpaSpecificationExecutor<InventorySupply> {

    List<InventorySupply> findAllBySupplyReference(String supplyReference);
}
