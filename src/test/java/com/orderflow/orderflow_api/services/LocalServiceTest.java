package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.models.Local;
import com.orderflow.orderflow_api.models.User;
import com.orderflow.orderflow_api.payload.CategoryDTO;
import com.orderflow.orderflow_api.payload.LocalDTO;
import com.orderflow.orderflow_api.payload.LocalResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class LocalServiceTest {

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
        MockitoAnnotations.openMocks(this);
        modelMapper = new ModelMapper();
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


    @DisplayName("JUnit test for Given Local Object List When Find All Locals Then Return Local Response Object")
    @Test
    void testGivenLocalObjectListWhenFindAllLocalsThenReturnLocalResponseObject() {
        // Given/Arrange
        given(authUtil.userOnLoggedSession()).willReturn(new User("username", "user@email.com", "hashPass", "firstName", "lastName"));

        List<Local> localMockList = List.of(
                localOne,
                localTwo,
                localThree,
                localFour);

        Page<Local> localMockPage = new PageImpl(localMockList, PageRequest.of(0, 10), localMockList.size());

        given(localRepository.findAll(any(PageRequest.class))).willReturn(localMockPage);

        // When/Act
        LocalResponse localResponse = localService.findAllLocals(0, 1, "streetName", "asc");

        // Then/Assert
        assertNotNull(localResponse);
        assertEquals(4L, localResponse.getTotalElements());
        assertEquals(1, localResponse.getTotalPages());
        assertEquals(0, localResponse.getPageNumber());
        assertTrue(localResponse.isLastPage());
    }

    @DisplayName("JUnit test for Given Local Object List When Find All User Locals Then Return Local Response Object ")
    @Test
    void testGivenLocalObjectListWhenFindAllUserLocalsThenReturnLocalResponseObject() {
        // Given/Arrange
        given(authUtil.userOnLoggedSession())
                .willReturn(new User("username", "user@email.com", "hashPass", "firstName", "lastName"));
        List<Local> localMockList = List.of(localOne, localTwo, localThree, localFour);
        Page<Local> localMockPage = new PageImpl(localMockList, PageRequest.of(0, 10), localMockList.size());
        given(localRepository.findAll(any(PageRequest.class))).willReturn(localMockPage);

        // When/Act
        LocalResponse localResponse = localService.findAllUserLocals(0, 1, "streetName", "asc");

        // Then/Assert
        assertNotNull(localResponse);
        assertEquals(4L, localResponse.getTotalElements());
        assertEquals(1, localResponse.getTotalPages());
        assertEquals(0, localResponse.getPageNumber());
        assertTrue(localResponse.isLastPage());
    }

}
