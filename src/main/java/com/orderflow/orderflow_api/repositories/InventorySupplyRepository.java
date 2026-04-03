package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.InventorySupply;
import com.orderflow.orderflow_api.models.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.Instant;
import java.util.List;

public interface InventorySupplyRepository extends JpaRepository<InventorySupply,Long>,  JpaSpecificationExecutor<InventorySupply> {

    List<InventorySupply> findAllBySupplyReference(String supplyReference);

    Page<InventorySupply> findByMovmentDateGreaterThanEqualAndMovmentDateLessThanEqual(Instant firstDate, Instant lastDate, Pageable pageDetails);
}
