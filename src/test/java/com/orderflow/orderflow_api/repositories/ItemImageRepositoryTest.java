package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.models.ItemImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemImageRepositoryTest {

    @Autowired
    private ItemImageRepository itemImageRepository;

    private ItemImage itemImageOne;
    private ItemImage itemImageTwo;
    private ItemImage itemImageThree;
    private ItemImage itemImageFour;

    @BeforeEach
    public void setup() {
        itemImageOne = new ItemImage(
                "/imageOne/uuidCode",
                "Item One",
                "Item One Description"
        );
        itemImageTwo = new ItemImage(
                "/imageTwo/uuidCode",
                "Item Two",
                "Item Two Description"
        );
        itemImageThree = new ItemImage(
                "/imageThree/uuidCode",
                "Item Three",
                "Item Three Description"
        );
        itemImageFour = new ItemImage(
                "/imageFour/uuidCode",
                "Item Four",
                "Item Four Description"
        );
    }

    @DisplayName("JUnit test for Given ItemImage Object when Save then Return ItemImage Object")
    @Test
    void testGivenItemImageObject_whenSave_thenReturnItemImageObject(){
        // Given/Arrange

        // When/Act
        ItemImage savedItemImage = itemImageRepository.save(itemImageOne);

        // Then/Assert
        assertNotNull(savedItemImage);
        assertTrue(savedItemImage.getItemImageId()>0);
        assertEquals("/imageOne/uuidCode", savedItemImage.getUrl());
        assertEquals("Item One", savedItemImage.getTitle());
        assertEquals("Item One Description", savedItemImage.getDescription());
    }

    @DisplayName("JUnit test for Given ItemImage Object when Find By Id then Return Item Object")
    @Test
    void testGivenItemImageObject_whenFindById_thenReturnItemObject(){
        // Given/Arrange
        itemImageRepository.save(itemImageOne);
        itemImageRepository.save(itemImageTwo);

        // When/Act
        ItemImage firstItemImage = itemImageRepository.findById(itemImageOne.getItemImageId()).get();
        ItemImage secondItemImage = itemImageRepository.findById(itemImageTwo.getItemImageId()).get();

        // Then/Assert
        assertNotNull(firstItemImage);
        assertNotNull(secondItemImage);
        assertEquals(itemImageOne.getItemImageId(), firstItemImage.getItemImageId());
        assertEquals(itemImageTwo.getItemImageId(), secondItemImage.getItemImageId());
    }

    @DisplayName("JUnit test for Given Item Object when Update ItemImage then Return ItemImage Object")
    @Test
    void testGivenItemObject_whenUpdateItem_thenReturnUpdateObject(){
        // Given/Arrange
        itemImageRepository.save(itemImageOne);

        // When/Act
        ItemImage savedItemImage = itemImageRepository.findById(itemImageOne.getItemImageId()).get();
        savedItemImage.setTitle("Changed Title");
        savedItemImage.setUrl("Changed Url");
        savedItemImage.setDescription("Changed Description");

        ItemImage updatedItemImage = itemImageRepository.save(savedItemImage);

        // Then/Assert
        assertNotNull(updatedItemImage);
        assertEquals("Changed Title", updatedItemImage.getTitle());
        assertEquals("Changed Url", updatedItemImage.getUrl());
        assertEquals("Changed Description", updatedItemImage.getDescription());
    }

    @DisplayName("JUnit test for Given Item Object when Delete ItemImageById then Remove ItemImage Object")
    @Test
    void testGivenItemImageObject_whenDeleteItemImageById_thenRemoveItemImage(){
        // Given/Arrange
        itemImageRepository.save(itemImageOne);

        // When/Act
        itemImageRepository.deleteById(itemImageOne.getItemImageId());
        Optional<ItemImage> itemImageOptional = itemImageRepository.findById(itemImageOne.getItemImageId());

        // Then/Assert
        assertTrue(itemImageOptional.isEmpty());
    }
}
