package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.SupplyEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SupplyEventRepository extends JpaRepository<SupplyEvent, Long> {

    List<SupplyEvent> findAllBySupplyId(Long supplyId);

    List<SupplyEvent> findByEventDateGreaterThanEqualAndEventDateLessThanEqual(Instant firstDate, Instant lastDate);
}
