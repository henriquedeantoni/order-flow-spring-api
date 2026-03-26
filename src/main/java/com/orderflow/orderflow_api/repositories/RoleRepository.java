package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Role;
import com.orderflow.orderflow_api.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(Roles role);
}
