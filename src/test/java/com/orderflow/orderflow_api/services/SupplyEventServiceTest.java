package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.Supply;
import com.orderflow.orderflow_api.models.SupplyEvent;
import com.orderflow.orderflow_api.payload.SupplyEventRequestDTO;
import com.orderflow.orderflow_api.payload.SupplyEventResponseDTO;
import com.orderflow.orderflow_api.repositories.SupplyEventRepository;
import com.orderflow.orderflow_api.repositories.SupplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SupplyEventServiceTest {

    @InjectMocks
    private SupplyEventServiceImpl supplyEventService;

    @Mock
    private SupplyEventRepository supplyEventRepository;

    @Mock
    private SupplyRepository supplyRepository;

    @Mock
    private ModelMapper modelMapper;

    private Supply supplyOne;

    private SupplyEvent supplyEventOne;
    private SupplyEvent supplyEventTwo;
    private SupplyEvent supplyEventThree;
    private SupplyEvent supplyEventFour;

    private OffsetDateTime dateTimeOne = OffsetDateTime.of(2025, 10, 10, 10, 10, 10, 0, ZoneOffset.UTC);
    private OffsetDateTime dateTimeTwo = OffsetDateTime.of(2025, 10, 11, 10, 10, 10, 0, ZoneOffset.UTC);
    private OffsetDateTime dateTimeThree = OffsetDateTime.of(2025, 10, 12, 10, 10, 10, 0, ZoneOffset.UTC);
    private OffsetDateTime dateTimeFour = OffsetDateTime.of(2025, 10, 13, 10, 10, 10, 0, ZoneOffset.UTC);

    @BeforeEach
    public void setUp()
    {
        supplyEventOne = new SupplyEvent(1L,1L,"STOCK_IN",100, dateTimeOne);
        supplyEventTwo = new SupplyEvent(2L,1L,"STOCK_IN",100, dateTimeTwo);
        supplyEventThree = new SupplyEvent(3L,1L,"STOCK_OUT",80, dateTimeThree);
        supplyEventFour = new SupplyEvent(4L,1L,"STOCK_IN",50, dateTimeFour);

        supplyOne = new Supply("name", "reference", "brand name", "supply description","supply code","gr" );
        supplyOne.setSupplyId(1L);

        given(modelMapper.map(any(SupplyEvent.class), eq(SupplyEventResponseDTO.class))).willAnswer( invocation -> {
            SupplyEvent source = invocation.getArgument(0);
            SupplyEventResponseDTO response = new SupplyEventResponseDTO();
            response.setSupplyId(source.getSupplyId());
            response.setEventType(source.getEventType());
            response.setQuantityMoved(source.getQuantityMoved());
            response.setEventDate(source.getEventDate());
            return response;
        });
    }

    @DisplayName("JUnit test for Given SupplyEvent Object when FirstSupplyEventRegister then Return SupplyEventDTO Object")
    @Test
    public void givenSupplyEventObjectWhenFirstSupplyEventRegisterThenReturnSupplyEventDTOObject()
    {
        // Given/arrange
        SupplyEventRequestDTO requestDTO = new SupplyEventRequestDTO();
        requestDTO.setSupplyId(1L);
        requestDTO.setEventType("STOCK_IN");
        requestDTO.setQuantityMoved(0);
        requestDTO.setAddDate(OffsetDateTime.now());

        SupplyEventResponseDTO responseDTO = new SupplyEventResponseDTO();
        responseDTO.setEventType("STOCK_IN");
        responseDTO.setQuantityMoved(0);
        responseDTO.setEventDate(OffsetDateTime.now());

        given(supplyRepository.findById(1L)).willReturn(Optional.of(supplyOne));

        // When/Act
        //given(modelMapper.map(requestDTO, SupplyEvent.class)).willReturn(supplyEventOne);
        when(modelMapper.map(any(SupplyEventRequestDTO.class), eq(SupplyEvent.class)))
                .thenReturn(supplyEventOne);
        when(modelMapper.map(any(SupplyEvent.class), eq(SupplyEventResponseDTO.class)))
                .thenReturn(responseDTO);

        SupplyEventResponseDTO response = supplyEventService.firstSupplyEventRegister(1L);

        // Then/Assert
        assertNotNull(response);
        assertEquals("STOCK_IN",response.getEventType());
        assertEquals(0,response.getQuantityMoved());
        verify(supplyEventRepository, times(1)).save(any());
    }

    @DisplayName("JUnit test for Given SupplyEvent Object when FirstSupplyEventRegister With Invalid SupplyId then Throws Resource not found Exception")
    @Test
    public void givenSupplyEventObjectWhenFirstSupplyEventRegisterWithInvalidSupplyIdThenThrowsResourceNotFoundException()
    {
        // Given/arrange
        Long validSupplyId = 1L;
        Long invalidSupplyId = 2L;
        SupplyEventRequestDTO requestDTO = new SupplyEventRequestDTO();
        requestDTO.setSupplyId(1L);
        requestDTO.setEventType("STOCK_IN");
        requestDTO.setQuantityMoved(0);
        requestDTO.setAddDate(OffsetDateTime.now());

        SupplyEventResponseDTO responseDTO = new SupplyEventResponseDTO();
        responseDTO.setEventType("STOCK_IN");
        responseDTO.setQuantityMoved(0);
        responseDTO.setEventDate(OffsetDateTime.now());

        given(supplyRepository.findById(validSupplyId)).willReturn(Optional.of(supplyOne));
        given(supplyRepository.findById(invalidSupplyId)).willReturn(Optional.empty());

        // When/Act
        //given(modelMapper.map(requestDTO, SupplyEvent.class)).willReturn(supplyEventOne);
        when(modelMapper.map(any(SupplyEventRequestDTO.class), eq(SupplyEvent.class)))
                .thenReturn(supplyEventOne);
        when(modelMapper.map(any(SupplyEvent.class), eq(SupplyEventResponseDTO.class)))
                .thenReturn(responseDTO);

        assertThrows(ResourceNotFoundException.class, ()-> supplyEventService.firstSupplyEventRegister(2L));

        // Then/Assert
        verify(supplyEventRepository, never()).save(any());
    }

    @DisplayName("JUnit test for Given SupplyEvent Object when Increase Quantity Moved Event then Return SupplyEventDTO Object")
    @Test
    public void givenSupplyEventObjectWhenIncreaseQuantityMovedEventThenReturnSupplyEventResponseDTOObject()
    {
        // Given/arrange
        SupplyEventRequestDTO requestDTO = new SupplyEventRequestDTO();
        requestDTO.setSupplyId(1L);
        requestDTO.setEventType("STOCK_IN");
        requestDTO.setQuantityMoved(0);
        requestDTO.setAddDate(OffsetDateTime.now());

        SupplyEventResponseDTO responseDTO = new SupplyEventResponseDTO();
        responseDTO.setEventType("STOCK_IN");
        responseDTO.setQuantityMoved(100);
        responseDTO.setEventDate(OffsetDateTime.now());

        given(supplyRepository.findById(1L)).willReturn(Optional.of(supplyOne));

        // When/Act
        when(modelMapper.map(any(SupplyEventRequestDTO.class), eq(SupplyEvent.class)))
                .thenReturn(supplyEventOne);
        when(modelMapper.map(any(SupplyEvent.class), eq(SupplyEventResponseDTO.class)))
                .thenReturn(responseDTO);

        SupplyEventResponseDTO response = supplyEventService.increaseQuantityMovedEvent(1L, 100);

        // Then/Assert
        assertNotNull(response);
        assertEquals("STOCK_IN",response.getEventType());
        assertEquals(100,response.getQuantityMoved());
        verify(supplyEventRepository, times(1)).save(any());
    }

    @DisplayName("JUnit test for Given SupplyEvent Object when Increase Quantity Moved Event With Wrong SupplyId then Throws ResourceNotFoundException")
    @Test
    public void givenSupplyEventObjectWhenIncreaseQuantityMovedEventWithWrongSupplyIdThenThrowsResourceNotFoundException()
    {
        // Given/arrange
        Long validSupplyId = 1L;
        Long invalidSupplyId = 2L;

        SupplyEventRequestDTO requestDTO = new SupplyEventRequestDTO();
        requestDTO.setSupplyId(1L);
        requestDTO.setEventType("STOCK_IN");
        requestDTO.setQuantityMoved(100);
        requestDTO.setAddDate(OffsetDateTime.now());

        SupplyEventResponseDTO responseDTO = new SupplyEventResponseDTO();
        responseDTO.setEventType("STOCK_IN");
        responseDTO.setQuantityMoved(100);
        responseDTO.setEventDate(OffsetDateTime.now());

        given(supplyRepository.findById(validSupplyId)).willReturn(Optional.of(supplyOne));
        given(supplyRepository.findById(invalidSupplyId)).willReturn(Optional.empty());

        // When/Act
        when(modelMapper.map(any(SupplyEventRequestDTO.class), eq(SupplyEvent.class)))
                .thenReturn(supplyEventOne);
        when(modelMapper.map(any(SupplyEvent.class), eq(SupplyEventResponseDTO.class)))
                .thenReturn(responseDTO);
        assertThrows(ResourceNotFoundException.class, ()-> supplyEventService.increaseQuantityMovedEvent(2L, 100));

        // Then/Assert
        verify(supplyEventRepository, never()).save(any());
    }


    @DisplayName("JUnit test for Given SupplyEvent Object when Decrease Quantity Moved Event then Return SupplyEventDTO Object")
    @Test
    public void givenSupplyEventObjectWhenDecreaseQuantityMovedEventThenReturnSupplyEventResponseDTOObject()
    {
        // Given/arrange
        SupplyEventRequestDTO requestDTO = new SupplyEventRequestDTO();
        requestDTO.setSupplyId(1L);
        requestDTO.setEventType("STOCK_OUT");
        requestDTO.setQuantityMoved(100);
        requestDTO.setAddDate(OffsetDateTime.now());

        SupplyEventResponseDTO responseDTO = new SupplyEventResponseDTO();
        responseDTO.setEventType("STOCK_OUT");
        responseDTO.setQuantityMoved(100);
        responseDTO.setEventDate(OffsetDateTime.now());

        given(supplyRepository.findById(1L)).willReturn(Optional.of(supplyOne));

        // When/Act
        when(modelMapper.map(any(SupplyEventRequestDTO.class), eq(SupplyEvent.class)))
                .thenReturn(supplyEventOne);
        when(modelMapper.map(any(SupplyEvent.class), eq(SupplyEventResponseDTO.class)))
                .thenReturn(responseDTO);

        SupplyEventResponseDTO response = supplyEventService.decreaseQuantityMovedEvent(1L, 100);

        // Then/Assert
        assertNotNull(response);
        assertEquals("STOCK_OUT",response.getEventType());
        assertEquals(100,response.getQuantityMoved());
        verify(supplyEventRepository, times(1)).save(any());
    }

    @DisplayName("JUnit test for Given SupplyEvent Object when Decrease Quantity Moved Event With Wrong SupplyId then Throws ResourceNotFoundException")
    @Test
    public void givenSupplyEventObjectWhenDecreaseQuantityMovedEventWithWrongSupplyIdThenThrowsResourceNotFoundException()
    {
        // Given/arrange
        Long validSupplyId = 1L;
        Long invalidSupplyId = 2L;

        SupplyEventRequestDTO requestDTO = new SupplyEventRequestDTO();
        requestDTO.setSupplyId(1L);
        requestDTO.setEventType("STOCK_IN");
        requestDTO.setQuantityMoved(100);
        requestDTO.setAddDate(OffsetDateTime.now());

        SupplyEventResponseDTO responseDTO = new SupplyEventResponseDTO();
        responseDTO.setEventType("STOCK_IN");
        responseDTO.setQuantityMoved(100);
        responseDTO.setEventDate(OffsetDateTime.now());

        given(supplyRepository.findById(validSupplyId)).willReturn(Optional.of(supplyOne));
        given(supplyRepository.findById(invalidSupplyId)).willReturn(Optional.empty());

        // When/Act
        when(modelMapper.map(any(SupplyEventRequestDTO.class), eq(SupplyEvent.class)))
                .thenReturn(supplyEventOne);
        when(modelMapper.map(any(SupplyEvent.class), eq(SupplyEventResponseDTO.class)))
                .thenReturn(responseDTO);
        assertThrows(ResourceNotFoundException.class, ()-> supplyEventService.decreaseQuantityMovedEvent(2L, 100));

        // Then/Assert
        verify(supplyEventRepository, never()).save(any());
    }

    @DisplayName("JUnit test for Given")
    @Test
    public void givenSupplyEventObjectsWhenGetSupplyEventListThenReturnSupplyEventResponseDTOList()
    {
        // Given/Arrange
        List<SupplyEvent> mockSupplyEventList = List.of(
                supplyEventOne,
                supplyEventTwo,
                supplyEventThree,
                supplyEventFour
        );
        given(supplyEventRepository.findAllBySupplyId(1L)).willReturn(mockSupplyEventList);

        // When/Act
        List<SupplyEventResponseDTO> response = supplyEventService.getSupplyEventList(1L);

        // Then/Assert
        assertNotNull(response);
        assertEquals("STOCK_IN",response.get(0).getEventType());
        assertEquals(100,response.get(0).getQuantityMoved());
        assertEquals("STOCK_IN",response.get(1).getEventType());
        assertEquals(100,response.get(1).getQuantityMoved());
        assertEquals("STOCK_OUT",response.get(2).getEventType());
        assertEquals(80,response.get(2).getQuantityMoved());
        assertEquals("STOCK_IN",response.get(3).getEventType());
        assertEquals(50,response.get(3).getQuantityMoved());
    }

}
