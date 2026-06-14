package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.AlbumImage;
import com.orderflow.orderflow_api.models.SimpleImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SimpleImageRepositoryTest {

    @Autowired
    private SimpleImageRepository simpleImageRepository;

    @Autowired
    private AlbumImageRepository albumImageRepository;

    private SimpleImage simpleImageOne;
    private SimpleImage simpleImageTwo;
    private SimpleImage simpleImageThree;
    private SimpleImage simpleImageFour;

    private AlbumImage albumImageOne;

    @BeforeEach
    public void setup() {

        albumImageOne = new AlbumImage(
                "Album Image One Title",
                "Album Image One Description"
        );

        albumImageRepository.saveAndFlush(albumImageOne);

        simpleImageOne = new SimpleImage(
                "/imageOne/uuidCode",
                "Simple Image One"
        );
        simpleImageTwo = new SimpleImage(
                "/imageTwo/uuidCode",
                "Simple Image Two"
        );
        simpleImageThree = new SimpleImage(
                "/imageThree/uuidCode",
                "Simple Image Three"
        );
        simpleImageFour = new SimpleImage(
                "/imageFour/uuidCode",
                "Simple Image Four"
        );

        simpleImageOne.setAlbumImage(albumImageOne);
        simpleImageTwo.setAlbumImage(albumImageOne);
        simpleImageThree.setAlbumImage(albumImageOne);
        simpleImageFour.setAlbumImage(albumImageOne);
    }

    @DisplayName("JUnit test for Given SimpleImage Object when Save then Return SimpleImage Object")
    @Test
    void testGivenSimpleImageObject_whenSave_thenReturnSimpleImageObject(){
        // Given/Arrange

        // When/Act
        SimpleImage savedSimpleImage = simpleImageRepository.save(simpleImageOne);

        // Then/Assert
        assertNotNull(savedSimpleImage);
        assertTrue(savedSimpleImage.getSimpleImageId()>0);
        assertEquals("/imageOne/uuidCode", savedSimpleImage.getUrl());
        assertEquals("Simple Image One", savedSimpleImage.getTitle());
    }

    @DisplayName("JUnit test for Given SimpleImage Object when Find By Id then Return Simple Image Object")
    @Test
    void testGivenSimpleImageObject_whenFindById_thenReturnSimpleImageObject(){
        // Given/Arrange
        simpleImageRepository.save(simpleImageOne);
        simpleImageRepository.save(simpleImageTwo);

        // When/Act
        SimpleImage firstSimpleImage = simpleImageRepository.findById(simpleImageOne.getSimpleImageId()).get();
        SimpleImage secondSimpleImage = simpleImageRepository.findById(simpleImageTwo.getSimpleImageId()).get();

        // Then/Assert
        assertNotNull(firstSimpleImage);
        assertNotNull(secondSimpleImage);
        assertEquals(simpleImageOne.getSimpleImageId(), firstSimpleImage.getSimpleImageId());
        assertEquals(simpleImageTwo.getSimpleImageId(), secondSimpleImage.getSimpleImageId());
    }

    @DisplayName("JUnit test for Given SimpleImage Object when Update SimpleImage then Return SimpleImage Object")
    @Test
    void testGivenSimpleImageObject_whenUpdateSimpleImage_thenReturnUpdateObject(){
        // Given/Arrange
        simpleImageRepository.save(simpleImageOne);

        // When/Act
        SimpleImage savedSimpleImage = simpleImageRepository.findById(simpleImageOne.getSimpleImageId()).get();
        savedSimpleImage.setTitle("Changed Title");
        savedSimpleImage.setUrl("Changed Url");

        SimpleImage updatedSimpleImage = simpleImageRepository.save(savedSimpleImage);

        // Then/Assert
        assertNotNull(updatedSimpleImage);
        assertEquals("Changed Title", updatedSimpleImage.getTitle());
        assertEquals("Changed Url", updatedSimpleImage.getUrl());
    }

    @DisplayName("JUnit test for Given SimpleImage Object when Delete SimpleImageById then Remove SimpleImage Object")
    @Test
    void testGivenSimpleImageObject_whenDeleteSimpleImageById_thenRemoveSimpleImage(){
        // Given/Arrange
        simpleImageRepository.save(simpleImageOne);

        // When/Act
        simpleImageRepository.deleteById(simpleImageOne.getSimpleImageId());
        Optional<SimpleImage> simpleImageOptional = simpleImageRepository.findById(simpleImageOne.getSimpleImageId());

        // Then/Assert
        assertTrue(simpleImageOptional.isEmpty());
    }
}
