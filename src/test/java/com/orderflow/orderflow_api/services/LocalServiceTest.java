package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.models.Local;
import com.orderflow.orderflow_api.models.User;
import com.orderflow.orderflow_api.payload.CategoryDTO;
import com.orderflow.orderflow_api.payload.LocalDTO;
import com.orderflow.orderflow_api.repositories.LocalRepository;
import com.orderflow.orderflow_api.security.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class LocalServiceTest {

    @InjectMocks
    private ModelMapper modelMapper;

    @Mock
    private LocalRepository localRepository;

    @Mock
    private AuthUtil authUtil;

    @InjectMocks
    private LocalServiceImpl localService;

    private Local localOne;
    private Local localTwo;
    private Local localThree;
    private Local localFour;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(localService, "modelMapper", new ModelMapper());

        localOne = new Local("Street A", "Building A", "City A", "State A", "Country A", "PostalCode A");
        localTwo = new Local("Street B", "Building B", "City B", "State B", "Country B", "PostalCode B");
        localThree = new Local("Street C", "Building C", "City C", "State C", "Country C", "PostalCode C");
        localFour = new Local("Street D", "Building D", "City D", "State D", "Country D", "PostalCode D");
    }

    @DisplayName("JUnit test for Given Local Object when save Local then Return Local Object")
    @Test
    void testGivenLocalObjectWhenSaveLocalThenReturnLocalObject() {
        // Given/Arrange
        given(authUtil.userOnLoggedSession()).willReturn(new User("username", "user@email.com", "hashPass", "firstName", "lastName"));
        given(localRepository.findById(anyLong())).willReturn(Optional.of(localOne));
        given(localRepository.save(any(Local.class))).willReturn(localOne);
        LocalDTO localOneDTO = new ModelMapper().map(localOne, LocalDTO.class);

        // When/Act
        LocalDTO savedLocal = localService.registerLocal(localOneDTO);

        // Then/Assert
        assertNotNull(savedLocal);
        assertEquals("Street A", savedLocal.getStreetName());
    }


}
