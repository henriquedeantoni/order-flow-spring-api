package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

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
        given(localRepository.findByUser(any(User.class), any(PageRequest.class))).willReturn(localMockPage);

        // When/Act
        LocalResponse localResponse = localService.findAllUserLocals(0, 1, "streetName", "asc");

        // Then/Assert
        assertNotNull(localResponse);
        assertEquals(4L, localResponse.getTotalElements());
        assertEquals(1, localResponse.getTotalPages());
        assertEquals(0, localResponse.getPageNumber());
        assertTrue(localResponse.isLastPage());
    }

    @DisplayName("JUnit test for Given Local Object When Find Local By Id then Return Local Object")
    @Test
    void testGivenLocalObjectWhenFindLocalByIdThenReturnLocalObject() {
        // Given/Arrange
        localOne.setLocalId(1L);
        given(localRepository.findById(1L)).willReturn(Optional.of(localOne));
        LocalDTO localOneDTO = new ModelMapper().map(localOne, LocalDTO.class);

        // When/Act
        LocalDTO savedLocal = localService.findLocalById(1L);

        // Then/Assert
        assertNotNull(savedLocal);
        assertEquals("Street A", savedLocal.getStreetName());
        assertEquals("Building A", savedLocal.getBuildingName());
        assertEquals("City A", savedLocal.getCity());
    }

    @DisplayName("JUnit test for Given Local Object When Find Local By Id Non Existent Then Throws Resource Not Found Exception")
    @Test
    void testGivenLocalObjectWhenFindLocalByIdNonExistentThenThrowsResourceNotFoundException() {
        // Given/Arrange
        Long validLongId = 1L;
        Long invalidLongId = 2L;

        localOne.setLocalId(validLongId);
        given(localRepository.findById(validLongId)).willReturn(Optional.of(localOne));
        given(localRepository.findById(invalidLongId)).willReturn(Optional.empty());
        LocalDTO localOneDTO = new ModelMapper().map(localOne, LocalDTO.class);

        // When/Act
        assertThrows(ResourceNotFoundException.class, () -> {
            localService.findLocalById(invalidLongId);
        });

        // Then/Assert
        verify(localRepository).findById(2L);
    }

    @DisplayName("JUnit test for Given Local Object When Find Locals By City Then Return Locals Object Response")
    @Test
    void testGivenLocalObjectWhenFindLocalLocalByCityThenReturnLocalsObjectResponse() {
        // Given/Arrange
        given(authUtil.userOnLoggedSession()).willReturn(new User("username", "user@email.com", "hashPass", "firstName", "lastName"));

        String city = "city name";
        localOne.setCity(city);
        localTwo.setCity(city);
        localThree.setCity(city);
        localFour.setCity(city);

        List<Local> localMockList = List.of(
                localOne,
                localTwo,
                localThree,
                localFour);

        Page<Local> localMockPage = new PageImpl(localMockList, PageRequest.of(0, 10), localMockList.size());
        given(localRepository.findAllByCity(eq(city), any(Pageable.class))).willReturn(localMockPage);

        // When/Act
        LocalResponse response = localService.findLocalsByCity(city, 0, 10, "streetName", "asc");

        // Then/Assert
        assertNotNull(response);
        assertEquals(4, response.getTotalElements());
        assertEquals("Street A", response.getContent().get(0).getStreetName());
    }

    @DisplayName("JUnit test for Given Local Object When Find Locals By City Non Existent Then Return Empty Page Response")
    @Test
    void testGivenLocalObjectWhenFindLocalLocalByCityNonExistentThenReturnEmptyPageResponse() {
        // Given/Arrange
        given(authUtil.userOnLoggedSession()).willReturn(new User("username", "user@email.com", "hashPass", "firstName", "lastName"));

        String validCity = "city name";
        String invalidCity = "wrong city name";

        localOne.setCity(validCity);
        localTwo.setCity(validCity);
        localThree.setCity(validCity);
        localFour.setCity(validCity);

        List<Local> localMockList = List.of(
                localOne,
                localTwo,
                localThree,
                localFour);

        List<Local> emptyMockList = List.of();

        Page<Local> localMockPage = new PageImpl(localMockList, PageRequest.of(0, 10), localMockList.size());
        Page<Local> emptyLocalMockPage = new PageImpl(emptyMockList, PageRequest.of(0, 10), emptyMockList.size());

        given(localRepository.findAllByCity(eq(validCity), any(Pageable.class))).willReturn(localMockPage);
        given(localRepository.findAllByCity(eq(invalidCity), any(Pageable.class))).willReturn(emptyLocalMockPage);

        // When/Act
        assertThrows(APIException.class, ()-> {
            localService.findLocalsByCity(invalidCity, 0, 10, "streetName", "asc");
        });

        // Then/Assert
        verify(localRepository).findAllByCity(eq(invalidCity), any(Pageable.class));
    }

    @DisplayName("JUnit test for Given Local Object When Find Locals By State Then Return Locals Object Response")
    @Test
    void testGivenLocalObjectWhenFindLocalLocalByStateThenReturnLocalsObjectResponse() {
        // Given/Arrange
        given(authUtil.userOnLoggedSession()).willReturn(new User("username", "user@email.com", "hashPass", "firstName", "lastName"));

        String state = "state name";
        localOne.setState(state);
        localTwo.setState(state);
        localThree.setState(state);
        localFour.setState(state);

        List<Local> localMockList = List.of(
                localOne,
                localTwo,
                localThree,
                localFour);

        List<Local> emptyMockList = List.of();

        Page<Local> localMockPage = new PageImpl(localMockList, PageRequest.of(0, 10), localMockList.size());
        given(localRepository.findAllByState(eq(state), any(Pageable.class))).willReturn(localMockPage);

        // When/Act
        LocalResponse response = localService.findLocalsByState(state, 0, 10, "streetName", "asc");

        // Then/Assert
        assertNotNull(response);
        assertEquals(4, response.getTotalElements());
        assertEquals("Street A", response.getContent().get(0).getStreetName());
        assertEquals("state name", response.getContent().get(1).getState());
    }

    @DisplayName("JUnit test for Given Local Object When Find Locals By State Then Non Existent Then Return Empty Page Response")
    @Test
    void testGivenLocalObjectWhenFindLocalLocalByStateNonExistentThenReturnEmptyPageResponse() {
        // Given/Arrange
        given(authUtil.userOnLoggedSession()).willReturn(new User("username", "user@email.com", "hashPass", "firstName", "lastName"));

        String validState = "state name";
        String invalidState = "wrong state name";

        localOne.setState(validState);
        localTwo.setState(validState);
        localThree.setState(validState);
        localFour.setState(validState);

        List<Local> localMockList = List.of(
                localOne,
                localTwo,
                localThree,
                localFour);

        List<Local> emptyMockList = List.of();

        Page<Local> localMockPage = new PageImpl(localMockList, PageRequest.of(0, 10), localMockList.size());
        Page<Local> emptyLocalMockPage = new PageImpl(emptyMockList, PageRequest.of(0, 10), localMockList.size());

        given(localRepository.findAllByState(eq(validState), any(Pageable.class))).willReturn(localMockPage);
        given(localRepository.findAllByState(eq(invalidState), any(Pageable.class))).willReturn(emptyLocalMockPage);

        // When/Act
        assertThrows(APIException.class, ()-> {
            localService.findLocalsByState(invalidState, 0, 10, "streetName", "asc");
        });

        // Then/Assert
        verify(localRepository).findAllByState(eq(invalidState), any(Pageable.class));
    }

    @DisplayName("JUnit test for Given Local Object When Update Local then Return Local DTO Object")
    @Test
    void testGivenLocalObjectWhenUpdateLocalThenReturnLocalObject() {
        // Given/Arrange
        localOne.setLocalId(1L);
        given(localRepository.findById(1L)).willReturn(Optional.of(localOne));
        given(localRepository.save(localOne)).willReturn(localOne);
        LocalDTO localOneDTO = new ModelMapper().map(localOne, LocalDTO.class);

        // When/Act
        localOneDTO.setStreetName("New Street");
        localOneDTO.setState("new state");
        LocalDTO savedLocal = localService.updateLocal(1L, localOneDTO);

        // Then/Assert
        assertNotNull(savedLocal);
        assertEquals("New Street", savedLocal.getStreetName());
        assertEquals("new state", savedLocal.getState());
    }

    @DisplayName("JUnit test for Given Local when Delete Local then Return Deleted Local DTO")
    @Test
    void testGivenLocalWhenDeleteCategoryThenReturnDeletedCategoryDTO() {
        // Given/Arrange
        localOne.setLocalId(1L);
        given(localRepository.findById(anyLong())).willReturn(Optional.of(localOne));
        willDoNothing().given(localRepository).deleteById(anyLong());

        // When/Act
        LocalDTO deletedLocalDTO =  localService.deleteLocal(localOne.getLocalId());

        // Then/Assert
        verify(localRepository, times(1)).delete(localOne);
        assertEquals("Street A", deletedLocalDTO.getStreetName());
        assertEquals("Building A", deletedLocalDTO.getBuildingName());
        assertEquals("City A", deletedLocalDTO.getCity());
    }

}
