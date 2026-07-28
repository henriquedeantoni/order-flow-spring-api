package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.InventorySupply;
import com.orderflow.orderflow_api.models.Supply;
import com.orderflow.orderflow_api.payload.InventorySupplyDTO;
import com.orderflow.orderflow_api.repositories.InventorySupplyRepository;
import com.orderflow.orderflow_api.repositories.SupplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class InventorySupplyTest {

    @InjectMocks
    private InventorySupplyServiceImpl inventorySupplyService;

    @Mock
    private InventorySupplyRepository inventorySupplyRepository;

    @Mock
    private SupplyRepository supplyRepository;

    @Mock
    private ModelMapper modelMapper;

    private InventorySupply inventorySupplyOne;
    private InventorySupply inventorySupplyTwo;
    private InventorySupplyDTO inventorySupplyDTO;

    private Supply supplyOne;

    private LocalDate newDate = LocalDate.of(2025, 10, 10);

    private OffsetDateTime dateTime = OffsetDateTime.of(2025, 10, 10, 10, 10, 10, 0, ZoneOffset.UTC);

    private OffsetDateTime movementDateTime = OffsetDateTime.of(2025, 10, 10, 10, 10, 10, 0, ZoneOffset.UTC);

    @BeforeEach
    public void setUp() {

        inventorySupplyOne = new InventorySupply("codeBarOne", "sectionA", "supplyReferenceOne", "Approved", newDate, movementDateTime, movementDateTime);

        supplyOne = new Supply("supplyOne", "supplyOneReference", "supplyOneName", "supplyOneDescription", "supplyOneCode", "gr");

        inventorySupplyDTO = modelMapper.map(supplyOne, InventorySupplyDTO.class);
    }

    @DisplayName("JUnit test for Given Inventory Supply Object When Register Supply On Inventory Then Return Inventory Supply DTO Object")
    @Test
    void testGivenInventorySupplyObjectWhenRegisterSupplyOnInventoryThenReturnInventorySupplyDTOObject() {
        InventorySupplyDTO dto = new InventorySupplyDTO();
        dto.setSupplyReference("SUP123");
        dto.setSection("A1");
        dto.setCodeBar("123456789");

        Supply supply = new Supply();
        supply.setSupplyReference("SUP123");

        given(supplyRepository.findBySupplyReference("SUP123")).willReturn(supply);
        given(inventorySupplyRepository.save(any(InventorySupply.class))).willAnswer(inv -> inv.getArgument(0));
        given(modelMapper.map(any(InventorySupply.class), eq(InventorySupplyDTO.class))).willReturn(dto);

        InventorySupplyDTO result = inventorySupplyService.registerSupplyOnInventory(dto);

        assertThat(result).isNotNull();
        assertThat(result.getSupplyReference()).isEqualTo("SUP123");
    }

    @DisplayName("JUnit test for Given Inventory Supply Object When Register Supply On Inventory With Supply Reference Non Existent Then Throws APIException")
    @Test
    void testGivenInventorySupplyObjectWhenRegisterSupplyOnInventoryWithSupplyReferenceNonExistentThenThrowsAPIException() {
        // Given/Arrange


        // When/Act


        // Then/Assert


    }
}
