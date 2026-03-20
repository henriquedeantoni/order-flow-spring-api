package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.SupplyEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplyEventRepository extends CrudRepository<SupplyEvent, Long> {

}
