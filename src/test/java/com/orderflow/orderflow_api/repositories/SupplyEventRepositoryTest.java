package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.SupplyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class SupplyEventRepositoryTest {

    @Autowired
    private SupplyEventRepository supplyEventRepository;

    private final OffsetDateTime firstDate = OffsetDateTime.of(
            2020, 1, 1, 12, 00, 0, 0, ZoneOffset.UTC
    );
    private final OffsetDateTime secondDate = OffsetDateTime.of(
            2020, 1, 2, 12, 00, 0, 0, ZoneOffset.UTC
    );
    private final OffsetDateTime thirdDate = OffsetDateTime.of(
            2020, 1, 3, 12, 00, 0, 0, ZoneOffset.UTC
    );
    private final OffsetDateTime fourthDate = OffsetDateTime.of(
            2020, 1, 4, 12, 00, 0, 0, ZoneOffset.UTC
    );

    private SupplyEvent supplyEventOne;
    private SupplyEvent supplyEventTwo;
    private SupplyEvent supplyEventThree;
    private SupplyEvent supplyEventFour;

    @BeforeEach
    public void setup() {
        supplyEventOne = new SupplyEvent(
                1L,
                "STOCK_IN",
                25,
                firstDate
        );

        supplyEventTwo = new SupplyEvent(
                1L,
                "STOCK_IN",
                50,
                secondDate
        );

        supplyEventThree = new SupplyEvent(
                1L,
                "STOCK_OUT",
                30,
                thirdDate
        );

        supplyEventFour = new SupplyEvent(
                1L,
                "STOCK_IN",
                35,
                fourthDate
        );

    }

    @DisplayName("JUnit test for Given Event Supply Object when save then Return Supply Event Object")
    @Test
    void testGivenSupplyEventObject_whenSave_thenReturnSupplyEventObject(){
        // Given/Arrange

        // When/Act
        SupplyEvent savedSupplyEvent = supplyEventRepository.save(supplyEventOne);

        // Then/Assert
        assertNotNull(savedSupplyEvent);
        assertTrue(savedSupplyEvent.getSupplyEventId()>0);
        assertEquals(1L , savedSupplyEvent.getSupplyId());
        assertEquals("STOCK_IN", savedSupplyEvent.getEventType());
        assertEquals(25, savedSupplyEvent.getQuantityMoved());
        assertEquals(firstDate, savedSupplyEvent.getEventDate());
    }

    @DisplayName("JUnit test for Given Supply Event Saved Object when Find by Id  then Return Supply Event Object")
    @Test
    void testGivenSupplyEventSavedObject_whenFindById_thenReturnSupplyEventObject(){
        // Given/Arrange
        supplyEventRepository.save(supplyEventOne);

        // When/Act
        SupplyEvent savedSupplyEvent = supplyEventRepository.findById(supplyEventOne.getSupplyEventId()).get();

        // Then/Assert
        assertNotNull(savedSupplyEvent);
        assertTrue(savedSupplyEvent.getSupplyEventId()>0);
        assertEquals(supplyEventOne.getSupplyId(), savedSupplyEvent.getSupplyId());
    }

    @DisplayName("JUnit test for Given Supply Event Object when Update Supply then Return Supply Event Object")
    @Test
    void testGivenSupplyEventObject_whenUpdateSupplyEvent_thenReturnUpdateObject(){
        // Given/Arrange
        supplyEventRepository.save(supplyEventOne);

        // When/Act
        SupplyEvent savedSupplyEvent = supplyEventRepository.findById(supplyEventOne.getSupplyEventId()).get();
        savedSupplyEvent.setSupplyId(2L);
        savedSupplyEvent.setEventType("STOCK_OUT");
        savedSupplyEvent.setQuantityMoved(30);

        SupplyEvent updatedSupply = supplyEventRepository.save(savedSupplyEvent);

        // Then/Assert
        assertNotNull(updatedSupply);
        assertEquals(2L, updatedSupply.getSupplyId());
        assertEquals("STOCK_OUT", updatedSupply.getEventType());
        assertEquals(30, updatedSupply.getQuantityMoved());
    }

    @DisplayName("JUnit test for Given Supply Event Object when Delete Supply Event then Remove Supply Event Object")
    @Test
    void testGivenSupplyObject_whenDeleteSupplyById_thenRemoveSupplyObject(){
        // Given/Arrange
        supplyEventRepository.save(supplyEventOne);

        // When/Act
        supplyEventRepository.deleteById(supplyEventOne.getSupplyEventId());
        Optional<SupplyEvent> supplyOptional = supplyEventRepository.findById(supplyEventOne.getSupplyEventId());

        // Then/Assert
        assertTrue(supplyOptional.isEmpty());
    }

}
