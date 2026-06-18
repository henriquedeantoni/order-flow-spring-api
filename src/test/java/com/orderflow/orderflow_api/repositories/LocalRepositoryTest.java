package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Local;
import com.orderflow.orderflow_api.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class LocalRepositoryTest {

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Nested
    class simpleOperations{

        private Local localOne;
        private Local localTwo;
        private Local localThree;
        private Local localFour;
        private Local localFive;
        private Local localSix;

        @BeforeEach
        void setUp(){
            // Given/Arrange
            localOne = new Local(
                    "Street One",
                    "Building One",
                    "Rio De Janeiro",
                    "Rio De Janeiro",
                    "Brazil",
                    "85020180"
            );
            localTwo = new Local(
                    "Street Two",
                    "Building Two",
                    "Rio De Janeiro",
                    "Rio De Janeiro",
                    "Brazil",
                    "85150210"
            );
            localThree = new Local(
                    "Street One",
                    "Building One",
                    "Sao Paulo",
                    "Sao Paulo",
                    "Brazil",
                    "44150150"
            );
            localFour = new Local(
                    "Street Two",
                    "Building Two",
                    "Sao Paulo",
                    "Sao Paulo",
                    "Brazil",
                    "44150150"
            );
            localFive = new Local(
                    "Street Three",
                    "Building Three",
                    "Sao Paulo",
                    "Sao Paulo",
                    "Brazil",
                    "44105020"
            );
            localSix = new Local(
                    "Street One",
                    "Building One",
                    "Los Angeles",
                    "California",
                    "United States",
                    "1023046134"
            );
        }

        @DisplayName("JUnit test for Given Local Object when Save then Return Saved Local Object")
        @Test
        void testGivenLocalObject_whenSave_thenReturnSavedLocalObject(){
            // Given/Arrange

            // When/Act
            Local savedLocal = localRepository.save(localOne);

            // Then/Assert
            assertNotNull(savedLocal);
            assertTrue(savedLocal.getLocalId()>0);
            assertEquals("Street One", savedLocal.getStreetName());
            assertEquals("Building One", savedLocal.getBuildingName());
            assertEquals("Rio De Janeiro", savedLocal.getCity());
            assertEquals("Rio De Janeiro", savedLocal.getState());
            assertEquals("Brazil", savedLocal.getCountry());
            assertEquals("85020180", savedLocal.getPostalCode());
        }

        @DisplayName("JUnit test for Given Local List when save then return Local List")
        @Test
        void testGivenLocalList_whenSave_thenReturnLocalList(){
            // Given/Arrange
            localRepository.save(localOne);
            localRepository.save(localTwo);
            localRepository.save(localThree);
            localRepository.save(localFour);
            localRepository.save(localFive);
            localRepository.save(localSix);

            // When/act
            List<Local> savedLocalList = localRepository.findAll();

            // Then/Assert
            assertNotNull(savedLocalList);
            assertTrue(savedLocalList.size()>0);
            assertEquals(6, savedLocalList.size());
        }

        @DisplayName("JUnit test for Given Local Object When Find By LocalId Then Return Local Object")
        @Test
        void testGivenLocalObject_whenFindByLocalId_thenReturnLocalObject(){
            // Given/Arrange
            localRepository.save(localOne);

            // When/Act
            Local savedLocal = localRepository.findById(localOne.getLocalId()).get();

            // Then/Assert
            assertNotNull(savedLocal);
            assertEquals(localOne.getLocalId(), savedLocal.getLocalId());
        }

        @DisplayName("JUnit test for Given Local Object when find LocalId then return Local Object")
        @Test
        void testGivenLocalObject_whenUpdateLocal_thenReturnUpdatedLocalObject(){
            // Given/Arrange
            localRepository.save(localOne);

            // When/Act
            Local savedLocal = localRepository.findById(localOne.getLocalId()).get();
            savedLocal.setStreetName("New Street Name");
            savedLocal.setBuildingName("New Building Name");
            savedLocal.setCity("New City");
            savedLocal.setState("New State");

            Local updatedLocal = localRepository.save(savedLocal);

            // Then/Assert
            assertNotNull(updatedLocal);
            assertEquals(localOne.getLocalId(), updatedLocal.getLocalId());
            assertEquals("New Street Name", updatedLocal.getStreetName());
            assertEquals("New Building Name", updatedLocal.getBuildingName());
            assertEquals("New City", updatedLocal.getCity());
            assertEquals("New State", updatedLocal.getState());
        }

        @DisplayName("JUnit test for Given Local Object when find LocalId then return Local Object")
        @Test
        void testGivenLocalObject_whenDelete_thenRemoveLocalObject(){
            // Given/Arrange
            localRepository.save(localOne);

            // When/Act
            localRepository.deleteById(localOne.getLocalId());

            Optional<Local> deletedLocal = localRepository.findById(localOne.getLocalId());

            // Then/Assert
            assertTrue(deletedLocal.isEmpty());
        }
    }

    @Nested
    class elaboratedOperations {

        private Pageable pageable;

        private Local localOne;
        private Local localTwo;
        private Local localThree;
        private Local localFour;
        private Local localFive;
        private Local localSix;

        private User userOne;
        private User userTwo;

        private String cityOne;
        private String cityTwo;
        private String stateOne;
        private String countryOne;

        @BeforeEach
        void setUp() {
            // Given/Arrange
            cityOne = "Rio De Janeiro";
            cityTwo = "Sao Paulo";
            stateOne = "Sao Paulo";
            countryOne = "Brazil";

            localOne = new Local(
                    "Street One",
                    "Building One",
                    "Rio De Janeiro",
                    "Rio De Janeiro",
                    "Brazil",
                    "85020180"
            );
            localTwo = new Local(
                    "Street Two",
                    "Building Two",
                    "Rio De Janeiro",
                    "Rio De Janeiro",
                    "Brazil",
                    "85150210"
            );
            localThree = new Local(
                    "Street One",
                    "Building One",
                    "Sao Paulo",
                    "Sao Paulo",
                    "Brazil",
                    "44150150"
            );
            localFour = new Local(
                    "Street Two",
                    "Building Two",
                    "Sao Paulo",
                    "Sao Paulo",
                    "Brazil",
                    "44150150"
            );
            localFive = new Local(
                    "Street Three",
                    "Building Three",
                    "Sao Paulo",
                    "Sao Paulo",
                    "Brazil",
                    "44105020"
            );
            localSix = new Local(
                    "Street One",
                    "Building One",
                    "Los Angeles",
                    "California",
                    "United States",
                    "1023046134"
            );

            userOne = new User(
                    "UserOne",
                    "userone@email.com",
                    "hashPass1",
                    "User",
                    "One");

            userTwo = new User(
                    "UserTwo",
                    "usertwo@email.com",
                    "hashPass2",
                    "User",
                    "Two");

            entityManager.persist(localOne);
            entityManager.persist(localTwo);
            entityManager.persist(localThree);
            entityManager.persist(localFour);
            entityManager.persist(localFive);

            pageable = PageRequest.of(0, 10);
        }

        @DisplayName("JUnit test Given Page List when Save then Return Page List FindAllByCity" )
        @Test
        void testGivenPageList_whenSave_thenReturnPageListFindAllByCity(){
            // Given/Arrange

            // When/Act
            Page<Local> locals = localRepository.findAllByCity(cityOne, pageable);

            // Then/Assert
            assertNotNull(locals);
            assertEquals(2, locals.getContent().size());
        }

        @DisplayName("JUnit test Given Page List when Save then Return Page List FindAllByState" )
        @Test
        void testGivenPageList_whenSave_thenReturnPageListfindAllByState(){
            // Given/Arrange

            // When/Act
            Page<Local> locals = localRepository.findAllByState(stateOne, pageable);

            // Then/Assert
            assertNotNull(locals);
            assertEquals(3, locals.getContent().size());
        }

        @DisplayName("JUnit test Given Page List when Save then Return Page List FindAllByStateAndByCountry" )
        @Test
        void testGivenPageList_whenSave_thenReturnPageListfindAllByStateAndByCountry(){
            // Given/Arrange

            // When/Act
            List<Local> locals = localRepository.findAllByStateAndByCountry(stateOne, countryOne);

            // Then/Assert
            assertNotNull(locals);
            assertEquals(3, locals.size());
        }

        @DisplayName("JUnit test for Given  ")
        @Test
        void testGivenStateAndCountry_whenFindJPQL_thenReturnLocalObjectList(){
            // Given/Arrange
            localRepository.save(localOne);
            localRepository.save(localTwo);
            localRepository.save(localThree);
            localRepository.save(localFour);
            localRepository.save(localFive);
            localRepository.save(localSix);

            String state = "Sao Paulo";
            String country = "Brazil";

            // When/Act
            List<Local> locals = localRepository.findAllByStateAndByCountry(state, country);

            // Then/Assert
            assertNotNull(locals);
            assertEquals(3, locals.size());

        }

    }
}
