package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.models.InventorySupply;
import com.orderflow.orderflow_api.models.Supply;
import com.orderflow.orderflow_api.payload.SupplyDTO;
import com.orderflow.orderflow_api.repositories.InventorySupplyRepository;
import com.orderflow.orderflow_api.repositories.SupplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SupplyServiceTest {

    @InjectMocks
    private SupplyServiceImpl supplyService;

    @Mock
    private InventorySupplyRepository inventorySupplyRepository;

    @Mock
    private SupplyRepository supplyRepository;

    @Mock
    private ModelMapper modelMapper;

    private Supply supplyOne = new Supply();
    private Supply supplyTwo = new Supply();
    private Supply supplyThree = new Supply();
    private Supply supplyFour = new Supply();

    private OffsetDateTime dateTime = OffsetDateTime.of(2025, 10, 10, 10, 10, 10, 0, ZoneOffset.UTC);

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        supplyOne.setSupplyReference("SupplyReferenceOne");
        supplyOne.setSupplyName("SupplyNameOne");
        supplyOne.setSupplyDescription("SupplyDescriptionOne");
        supplyOne.setSupplyReference("SupplyReferenceOne");
        supplyOne.setBrandName("BrandNameOne");
        supplyOne.setSupplyCode("SupplyCodeOne");
        supplyOne.setSupplyUnit("SupplyUnitOne");
        supplyOne.setAddDate(dateTime);

        supplyTwo.setSupplyReference("SupplyReferenceTwo");
        supplyTwo.setSupplyName("SupplyNameTwo");
        supplyTwo.setSupplyDescription("SupplyDescriptionTwo");
        supplyTwo.setSupplyReference("SupplyReferenceTwo");
        supplyTwo.setBrandName("BrandNameTwo");

        supplyThree.setSupplyReference("SupplyReferenceThree");
        supplyThree.setSupplyName("SupplyNameThree");
        supplyThree.setSupplyDescription("SupplyDescriptionThree");
        supplyThree.setSupplyReference("SupplyReferenceThree");
        supplyThree.setBrandName("BrandNameThree");
    }

    @DisplayName("JUnit test for Given Supply Object when Register Supply then Return Supply DTO Object")
    @Test
    public void testGivenSupplyObjectWhenRegisterSupplyThenReturnSupplyDTOObject(){
        // Given/Arrange

        SupplyDTO supplyDTO = new SupplyDTO();
        supplyDTO.setSupplyReference("SupplyReferenceOne");
        supplyDTO.setSupplyName("SupplyNameOne");
        given(supplyRepository.findBySupplyReference(supplyDTO.getSupplyReference())).willReturn(null);
        given(supplyRepository.save(supplyOne)).willReturn(supplyOne);

        // When/Act
        when(modelMapper.map(supplyDTO, Supply.class)).thenReturn(supplyOne);
        when(modelMapper.map(supplyOne, SupplyDTO.class)).thenReturn(supplyDTO);
        SupplyDTO savedSupply = supplyService.registerSupply(supplyDTO);

        // Then/Assert
        assertNotNull(savedSupply);
        assertEquals("SupplyReferenceOne", savedSupply.getSupplyReference());
        assertEquals("SupplyNameOne", savedSupply.getSupplyName());
        verify(supplyRepository, times(1)).save(any());
    }

    @DisplayName("JUnit test for Given Supply Object when Register Supply with supply reference already registered then Return RuntimeException")
    @Test
    public void testGivenSupplyObjectWhenRegisterSupplyWithSupplyReferenceAlreadyRegisteredThenReturnRuntimeException(){
        // Given/Arrange
        SupplyDTO supplyDTO = new SupplyDTO();
        supplyDTO.setSupplyReference("SupplyReferenceOne");
        supplyDTO.setSupplyName("SupplyNameOne");
        given(supplyRepository.findBySupplyReference(supplyDTO.getSupplyReference())).willReturn(supplyOne);
        given(supplyRepository.save(supplyOne)).willReturn(supplyOne);

        // When/Act
        when(modelMapper.map(supplyDTO, Supply.class)).thenReturn(supplyOne);
        when(modelMapper.map(supplyOne, SupplyDTO.class)).thenReturn(supplyDTO);
        assertThrows(APIException.class, ()-> supplyService.registerSupply(supplyDTO));

        // Then/Assert
        verify(supplyRepository, never()).save(any());
    }

    @DisplayName("JUnit test for Given Supply Object when Update Supply then Return Supply DTO Object")
    @Test
    public void testGivenSupplyObjectWhenUpdateSupplyThenReturnSupplyDTOObject(){
        // Given/Arrange
        Long validId = 1L;
        supplyOne.setSupplyId(validId);

        SupplyDTO supplyDTO = new SupplyDTO();
        supplyDTO.setSupplyReference("SupplyReferenceOne");
        supplyDTO.setSupplyName("SupplyNameOne");
        given(supplyRepository.findById(validId)).willReturn(Optional.of(supplyOne));
        given(supplyRepository.findBySupplyReference(supplyDTO.getSupplyReference())).willReturn(supplyOne);
        given(supplyRepository.save(supplyOne)).willReturn(supplyOne);

        // When/Act
        when(modelMapper.map(supplyDTO, Supply.class)).thenReturn(supplyOne);
        when(modelMapper.map(supplyOne, SupplyDTO.class)).thenReturn(supplyDTO);
        supplyDTO.setSupplyReference("SupplyReferenceOne Changed");
        supplyDTO.setSupplyName("SupplyNameOne Changed");

        SupplyDTO updatedSupply = supplyService.updateSupply(1L, supplyDTO);

        // Then/Assert
        assertNotNull(updatedSupply);
    }
}
