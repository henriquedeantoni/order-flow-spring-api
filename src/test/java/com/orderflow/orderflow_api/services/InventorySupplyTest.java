package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.models.InventorySupply;
import com.orderflow.orderflow_api.models.Supply;
import com.orderflow.orderflow_api.payload.InventoryResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
    private InventorySupply inventorySupplyThree;
    private InventorySupply inventorySupplyFour;

    private InventorySupplyDTO inventorySupplyDTO;

    private Supply supplyOne;

    private LocalDate newDate = LocalDate.of(2025, 10, 10);

    private OffsetDateTime dateTime = OffsetDateTime.of(2025, 10, 10, 10, 10, 10, 0, ZoneOffset.UTC);

    private OffsetDateTime movementDateTime = OffsetDateTime.of(2025, 10, 10, 10, 10, 10, 0, ZoneOffset.UTC);

    @BeforeEach
    public void setUp() {

        inventorySupplyOne = new InventorySupply("codeBarOne", "sectionA", "supplyReferenceOne", "Approved", newDate, movementDateTime, movementDateTime);

        inventorySupplyTwo = new InventorySupply("codeBarTwo", "sectionA", "supplyReferenceTwo", "Approved", newDate, movementDateTime, movementDateTime);

        inventorySupplyThree = new InventorySupply("codeBarThree", "sectionA", "supplyReferenceThree", "Approved", newDate, movementDateTime, movementDateTime);

        inventorySupplyFour = new InventorySupply("codeBarFour", "sectionA", "supplyReferenceFour", "Approved", newDate, movementDateTime, movementDateTime);

        inventorySupplyDTO = new InventorySupplyDTO();

        inventorySupplyDTO.setSupplyReference("supplyReferenceOne");
        inventorySupplyDTO.setSection("sectionA");
        inventorySupplyDTO.setCodeBar("codeBarOne");
        inventorySupplyDTO.setValDate(newDate);

        supplyOne = new Supply("supplyOne", "supplyOneReference", "supplyOneName", "supplyOneDescription", "supplyOneCode", "gr");

    }

    @DisplayName("JUnit test for Given Inventory Supply Object When Register Supply On Inventory Then Return Inventory Supply DTO Object")
    @Test
    void testGivenInventorySupplyObjectWhenRegisterSupplyOnInventoryThenReturnInventorySupplyDTOObject() {

        // Given/Arrange

        supplyOne.setSupplyReference("supplyReferenceOne");
        given(supplyRepository.findBySupplyReference("supplyReferenceOne")).willReturn(supplyOne);
        given(inventorySupplyRepository.save(any(InventorySupply.class))).willAnswer(inv -> inv.getArgument(0));
        given(modelMapper.map(any(InventorySupply.class), eq(InventorySupplyDTO.class))).willReturn(inventorySupplyDTO);

        // When/Act
        InventorySupplyDTO savedInventorySupply = inventorySupplyService.registerSupplyOnInventory(inventorySupplyDTO);

        // Then/Assert
        assertNotNull(savedInventorySupply);
        assertEquals("supplyReferenceOne",  savedInventorySupply.getSupplyReference());

    }

    @DisplayName("JUnit test for Given Inventory Supply Object When Register Supply On Inventory With Supply Reference Non Existent Then Throws APIException")
    @Test
    void testGivenInventorySupplyObjectWhenRegisterSupplyOnInventoryWithSupplyReferenceNonExistentThenThrowsAPIException() {
        // Given/Arrange
        String invalidSupplyReference = "invalidSupplyReference";
        inventorySupplyDTO.setSupplyReference(invalidSupplyReference);
        given(supplyRepository.findBySupplyReference("supplyReferenceOne")).willReturn(supplyOne);
        given(supplyRepository.findBySupplyReference(invalidSupplyReference)).willReturn(null);
        given(inventorySupplyRepository.save(any(InventorySupply.class))).willAnswer(inv -> inv.getArgument(0));
        given(modelMapper.map(any(InventorySupply.class), eq(InventorySupplyDTO.class))).willReturn(inventorySupplyDTO);

        // When/Act
        assertThrows(APIException.class, ()-> inventorySupplyService.registerSupplyOnInventory(inventorySupplyDTO));

        // Then/Assert
        verify(inventorySupplyRepository, never()).save(any(InventorySupply.class));
    }

    @DisplayName("JUnit test for Given Inventory List When Get All Inventory Itens Then Return Inventory Response")
    @Test
    void testGivenInventoryListWhenGetAllInventoryItensThenReturnInventoryResponse() {
        // Given/Arrange
        List<InventorySupply> inventorySupplyList = List.of(
                inventorySupplyOne,
                inventorySupplyTwo,
                inventorySupplyThree,
                inventorySupplyFour
        );

        Page<InventorySupply> pageMock = new PageImpl(inventorySupplyList, PageRequest.of(0, 10), 0);

        given(inventorySupplyRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(pageMock);

        // When/Act
        InventoryResponse response = inventorySupplyService.getAllInventoryItems(10, 0, "codeBar", "asc");

        // Then/Assert
        assertNotNull(response);
        assertEquals(4L, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getPageNumber());
    }
}
