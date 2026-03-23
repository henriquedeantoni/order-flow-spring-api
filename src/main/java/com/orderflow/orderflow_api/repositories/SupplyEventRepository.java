package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.SupplyEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SupplyEventRepository extends CrudRepository<SupplyEvent, Long> {

    @Query("SELECT * FROM supply_event AS se WHERE se.supplyId = ?1")
    List<SupplyEvent> findAllBySupplyId(Long supplyId);

    List<SupplyEvent> findByEventDateGreaterThanEqualAndIncludedDateLessThanEqual(Instant firstDate, Instant lastDate);
}
