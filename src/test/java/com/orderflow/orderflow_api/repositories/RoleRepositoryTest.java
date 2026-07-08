package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Role;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    private Role roleAdminOne;
    private Role roleClientOne;

    @BeforeEach
    public void setup() {

    }
}
