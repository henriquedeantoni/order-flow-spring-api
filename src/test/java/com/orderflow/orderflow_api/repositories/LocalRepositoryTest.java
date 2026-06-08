package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Local;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class LocalRepositoryTest {

    @Autowired
    private LocalRepository localRepository;

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

}
