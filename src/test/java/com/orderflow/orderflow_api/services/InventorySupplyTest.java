package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.InventorySupply;
import com.orderflow.orderflow_api.models.Supply;
import com.orderflow.orderflow_api.payload.InventorySupplyDTO;
import com.orderflow.orderflow_api.repositories.SupplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class InventorySupplyTest {

    @Mock
    private InventorySupply inventorySupply;

    @Mock
    private InventorySupplyServiceImpl inventorySupplyService;

    @Mock
    private SupplyRepository supplyRepository;

    private InventorySupply inventorySupplyOne;
    private InventorySupply inventorySupplyTwo;

    private Supply supplyOne;

    private LocalDate newDate = LocalDate.of(2025, 10, 10);

    private OffsetDateTime dateTime = OffsetDateTime.of(2025, 10, 10, 10, 10, 10, 0, ZoneOffset.UTC);

    private OffsetDateTime movementDateTime = OffsetDateTime.of(2025, 10, 10, 10, 10, 10, 0, ZoneOffset.UTC);

    @BeforeEach
    public void setUp() {

        inventorySupplyOne = new InventorySupply("codeBarOne", "sectionA", "supplyReferenceOne", "Approved", newDate, movementDateTime, movementDateTime);

        supplyOne = new Supply("supplyOne", "supplyOneReference", "supplyOneName", "supplyOneDescription", "supplyOneCode", "gr");
    }

    @DisplayName("JUnit test for Given Inventory Supply Object When Register Supply On Inventory Then Return Inventory Supply DTO Object")
    @Test
    void testGivenInventorySupplyObjectWhenRegisterSupplyOnInventoryThenReturnInventorySupplyDTOObject() {
        // Given/Arrange
        String validSupplyReference = "supplyOneReference";
        inventorySupplyOne.setSupplyReference(validSupplyReference);

        given(supplyRepository.findBySupplyReference(validSupplyReference)).willReturn(Optional.of(supplyOne));

        // When/Act


        // Then/Assert

    }

    @DisplayName("JUnit test for Given Inventory Supply Object When Register Supply On Inventory With Supply Reference Non Existent Then Throws APIException")
    @Test
    void testGivenInventorySupplyObjectWhenRegisterSupplyOnInventoryWithSupplyReferenceNonExistentThenThrowsAPIException() {
        // Given/Arrange


        // When/Act


        // Then/Assert


    }
}
