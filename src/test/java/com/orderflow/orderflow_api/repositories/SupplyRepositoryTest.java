package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.SimpleImage;
import com.orderflow.orderflow_api.models.Supply;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SupplyRepositoryTest {

    @Autowired
    private SupplyRepository supplyRepository;

    private Supply supplyOne;
    private Supply supplyTwo;
    private Supply supplyThree;
    private Supply supplyFour;

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


        supplyFour = new Supply(
                "Supply Four Name",
                "123RefFour",
                "Brand Four",
                "Description Four",
                "SupplyFourCode",
                "gr");

    }

    @DisplayName("JUnit test for Given Supply Object when save then Return Supply Object")
    @Test
    void testGivenSupplyObject_whenSave_thenReturnSupplyObject(){
        // Given/Arrange

        // When/Act
        Supply savedSupply = supplyRepository.save(supplyOne);

        // Then/Assert
        assertNotNull(savedSupply);
        assertTrue(savedSupply.getSupplyId()>0);
        assertEquals("Supply One Name", savedSupply.getSupplyName());
        assertEquals("123RefOne", savedSupply.getSupplyReference());
        assertEquals("Brand One", savedSupply.getBrandName());
        assertEquals("Description One", savedSupply.getSupplyDescription());
        assertEquals("SupplyOneCode", savedSupply.getSupplyCode());
        assertEquals("gr", savedSupply.getSupplyUnit());
    }

    @DisplayName("JUnit test for Given Supply Saved Object when Find by Id  then Return Supply Object")
    @Test
    void testGivenSupplySavedObject_whenFindById_thenReturnSupplyObject(){
        // Given/Arrange
        supplyRepository.save(supplyOne);

        // When/Act
        Supply savedSupply = supplyRepository.findById(supplyOne.getSupplyId()).get();

        // Then/Assert
        assertNotNull(savedSupply);
        assertTrue(savedSupply.getSupplyId()>0);
        assertEquals(supplyOne.getSupplyId(), savedSupply.getSupplyId());
    }

    @DisplayName("JUnit test for Given Supply Saved Object when Find by Reference then Return Supply Object")
    @Test
    void testGivenSupplySavedObject_whenFindByReference_thenReturnSupplyObject(){
        // Given/Arrange
        supplyRepository.save(supplyOne);

        // When/Act
        Supply savedSupply = supplyRepository.findBySupplyReference(supplyOne.getSupplyReference());

        // Then/Assert
        assertNotNull(savedSupply);
        assertTrue(savedSupply.getSupplyId()>0);
        assertEquals(supplyOne.getSupplyId(), savedSupply.getSupplyId());
    }


    @DisplayName("JUnit test for Given Supply Object when Update Supply then Return Supply Object")
    @Test
    void testGivenSupplyObject_whenUpdateSupply_thenReturnUpdateObject(){
        // Given/Arrange
        supplyRepository.save(supplyOne);

        // When/Act
        Supply savedSupply = supplyRepository.findById(supplyOne.getSupplyId()).get();
        savedSupply.setSupplyCode("Changed code");
        savedSupply.setSupplyDescription("Changed Description");
        savedSupply.setSupplyUnit("Changed unit");
        savedSupply.setSupplyName("Changed Name");
        savedSupply.setSupplyReference("Changed Reference");
        savedSupply.setBrandName("Changed Brand");

        Supply updatedSupply = supplyRepository.save(savedSupply);

        // Then/Assert
        assertNotNull(updatedSupply);
        assertEquals("Changed code", updatedSupply.getSupplyCode());
        assertEquals("Changed Description", updatedSupply.getSupplyDescription());
        assertEquals("Changed unit", updatedSupply.getSupplyUnit());
        assertEquals("Changed Name", updatedSupply.getSupplyName());
        assertEquals("Changed Reference", updatedSupply.getSupplyReference());
        assertEquals("Changed Brand", updatedSupply.getBrandName());
    }

    @DisplayName("JUnit test for Given Supply Object when Delete Supply then Remove Supply Object")
    @Test
    void testGivenSupplyObject_whenDeleteSupplyById_thenRemoveSupplyObject(){
        // Given/Arrange
        supplyRepository.save(supplyOne);

        // When/Act
        supplyRepository.deleteById(supplyOne.getSupplyId());
        Optional<Supply> supplyOptional = supplyRepository.findById(supplyOne.getSupplyId());

        // Then/Assert
        assertTrue(supplyOptional.isEmpty());
    }
}
