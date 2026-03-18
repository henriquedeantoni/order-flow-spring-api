package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Supply;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplyRepository extends CrudRepository<Supply, Long> {
}
