package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.InventorySupply;
import com.orderflow.orderflow_api.models.Supply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class InventorySupplyRepositoryTest {

    @Autowired
    private InventorySupplyRepository inventorySupplyRepository;

    @Autowired
    private SupplyRepository supplyRepository;

    private Supply supplyOne;
    private Supply supplyTwo;
    private Supply supplyThree;

    private InventorySupply inventorySupplyOne;
    private InventorySupply inventorySupplyTwo;
    private InventorySupply inventorySupplyThree;

    private final OffsetDateTime firstDate = OffsetDateTime.of(
            2025, 1, 1, 12, 00, 0, 0, ZoneOffset.UTC
    );

    private final OffsetDateTime secondDate = OffsetDateTime.of(
            2025, 1, 15, 12, 00, 0, 0, ZoneOffset.UTC
    );

    LocalDate valDateOne = LocalDate.of(2025, 10, 1);
    LocalDate valDatedateTwo = LocalDate.of(2025, 11, 8);
    LocalDate valDatedateThree = LocalDate.of(2025, 12, 15);

    @BeforeEach
    public void setup() {
        supplyOne = new Supply(
                "Supply One Name",
                "123RefOne",
                "Brand One",
                "Description One",
                "SupplyOneCode",
                "gr");

        supplyTwo = new Supply(
                "Supply Two Name",
                "123RefTwo",
                "Brand Two",
                "Description Two",
                "SupplyTwoCode",
                "gr");

        supplyThree = new Supply(
                "Supply Three Name",
                "123RefThree",
                "Brand Three",
                "Description Three",
                "SupplyThreeCode",
                "gr");

        inventorySupplyOne = new InventorySupply(
                "codeBarOne",
                "sectionOne",
                "supplyReferenceOne",
                "STOCK_IN",
                valDateOne,
                firstDate,
                firstDate
        );

        inventorySupplyTwo = new InventorySupply(
                "codeBarTwo",
                "sectionOne",
                "supplyReferenceTwo",
                "STOCK_IN",
                valDateOne,
                secondDate,
                secondDate
        );
    }

    @DisplayName("JUnit test for Given Inventory Supply Object when save then Return Inventory Supply Object")
    @Test
    void testGivenInventorySupplyObject_whenSave_thenReturnInventoryObject(){
        // Given/Arrange
        inventorySupplyOne.setSupply(supplyOne);
        supplyRepository.save(supplyOne);

        // When/Act
        InventorySupply savedInventorySupply = inventorySupplyRepository.save(inventorySupplyOne);

        // Then/Assert
        assertNotNull(savedInventorySupply);
        assertTrue(savedInventorySupply.getInventorySupplyId()>0);
        assertEquals("codeBarOne", savedInventorySupply.getCodeBar());
        assertEquals("sectionOne", savedInventorySupply.getSection());
        assertEquals("supplyReferenceOne", savedInventorySupply.getSupplyReference());
        assertEquals("STOCK_IN", savedInventorySupply.getStatus());
        assertEquals(supplyOne, savedInventorySupply.getSupply());
    }
}
