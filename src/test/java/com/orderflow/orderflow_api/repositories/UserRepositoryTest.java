package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Role;
import com.orderflow.orderflow_api.models.Roles;
import com.orderflow.orderflow_api.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User userAdminOne;
    private User userClientOne;

    private Role roleAdminOne;
    private Role roleClientOne;

    @BeforeEach
    void setup() {
        roleAdminOne = new Role();
        roleClientOne = new Role();

        roleAdminOne.setRoleName(Roles.ROLE_ADMIN);
        roleClientOne.setRoleName(Roles.ROLE_CLIENT);

        roleRepository.save(roleAdminOne);
        roleRepository.save(roleClientOne);

        Set<Role> rolesConfigOne = new HashSet<>();
        rolesConfigOne.add(roleAdminOne);
        rolesConfigOne.add(roleClientOne);

        Set<Role> rolesConfigTwo = new HashSet<>();
        rolesConfigOne.add(roleClientOne);

        userAdminOne = new User(
                "UserNameAdminOne",
                "useradminone@email.com",
                "hashPassOne",
                "firstNameOne",
                "lastNameOne"
        );

        userClientOne = new User(
                "UserNameClientOne",
                "userclientone@email.com",
                "hashPassTwo",
                "firstNameTwo",
                "lastNameTwo"
        );

        userAdminOne.setRoles(rolesConfigOne);
        userClientOne.setRoles(rolesConfigTwo);
    }

    @DisplayName("JUnit test for Given User Object when save then Return User Object")
    @Test
    void testGivenCartObject_whenSave_thenReturnCartObject(){
        // Given/Arrange

        // When/Act
        User savedUser = userRepository.save(userAdminOne);

        // Then/Assert
        assertNotNull(savedUser);
        assertTrue(savedUser.getUserId()>0);
        assertEquals(userAdminOne.getUserId(), savedUser.getUserId());
        assertEquals("UserNameAdminOne", savedUser.getUsername());
    }

}
