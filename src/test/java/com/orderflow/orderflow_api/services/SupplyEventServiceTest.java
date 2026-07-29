package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.SupplyEvent;
import com.orderflow.orderflow_api.repositories.SupplyEventRepository;
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

@ExtendWith(SpringExtension.class)
public class SupplyEventServiceTest {

    @InjectMocks
    private SupplyEventServiceImpl supplyEventService;

    @Mock
    private SupplyEventRepository supplyEventRepository;

    @Mock
    private ModelMapper modelMapper;

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
    }

    @DisplayName("JUnit test for Given SupplyEvent Object when FirstSupplyEventRegister then Return SupplyEventDTO Object")
    @Test
    public void givenSupplyEventObjectWhenFirstSupplyEventRegisterThenReturnSupplyEventDTOObject()
    {

    }
}
