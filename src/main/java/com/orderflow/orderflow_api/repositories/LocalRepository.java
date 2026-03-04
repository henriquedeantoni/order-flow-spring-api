package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.models.Local;
import com.orderflow.orderflow_api.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LocalRepository extends JpaRepository<Local, Long> , JpaSpecificationExecutor<Local> {
    Page<Local> findByUser(User user, Pageable pageDetails);
}
