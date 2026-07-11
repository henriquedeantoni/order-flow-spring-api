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
import java.util.Optional;
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
    void testGivenUserObject_whenSave_thenReturnCartObject(){
        // Given/Arrange

        // When/Act
        User savedUser = userRepository.save(userAdminOne);

        // Then/Assert
        assertNotNull(savedUser);
        assertTrue(savedUser.getUserId()>0);
        assertEquals(userAdminOne.getUserId(), savedUser.getUserId());
        assertEquals("UserNameAdminOne", savedUser.getUsername());
    }

    @DisplayName("JUnit test for Given User Object when find By Id then Return User Object")
    @Test
    void testGivenUserObject_whenFindById_thenReturnUserObject(){
        // Given/Arrange
        userRepository.save(userAdminOne);

        // When/Act
        User savedUser = userRepository.findById(userAdminOne.getUserId()).get();

        // Then/Assert
        assertNotNull(savedUser);
        assertTrue(savedUser.getUserId()>0);
        assertEquals(userAdminOne.getUserId(), savedUser.getUserId());
        assertEquals("UserNameAdminOne", savedUser.getUsername());
    }

    @DisplayName("JUnit test for Given User Object when update object then Return updated User Object")
    @Test
    void testGivenUserObject_whenUpdate_ThenReturnUpdatedObject(){
        // Given/Arrange
        userRepository.save(userAdminOne);

        // When/Act
        User savedUser = userRepository.findById(userAdminOne.getUserId()).get();
        savedUser.setEmail("new email");
        savedUser.setLastName("Changed last name");

        User updatedUser = userRepository.save(savedUser);

        // Then/Assert
        assertNotNull(updatedUser);
        assertEquals(userAdminOne.getUserId(), updatedUser.getUserId());
        assertEquals("new email", updatedUser.getEmail());
        assertEquals("Changed last name", updatedUser.getLastName());
    }

    @DisplayName("JUnit test for Given User Object when delete by Id object then Remove User Object")
    @Test
    void testGivenUserObject_whenDelete_ThenReturnUpdatedObject(){
        // Given/Arrange
        userRepository.save(userAdminOne);

        // When/Act
        userRepository.deleteById(userAdminOne.getUserId());
        Optional<User> userOptional = userRepository.findById(userAdminOne.getUserId());

        // Then/Assert
        assertTrue(userOptional.isEmpty());
    }


    @DisplayName("JUnit test for Given User Object when find by username then Return User Object")
    @Test
    void testGivenUserObject_whenFindByUserName_ThenReturnUserObject(){
        // Given/Arrange
        userRepository.save(userAdminOne);

        // When/Act
        User savedUser = userRepository.findByUsername(userAdminOne.getUsername()).get();

        // Then/Assert
        assertNotNull(savedUser);
        assertTrue(savedUser.getUserId()>0);
        assertEquals(userAdminOne.getUserId(), savedUser.getUserId());
        assertEquals("UserNameAdminOne", savedUser.getUsername());
    }

}
