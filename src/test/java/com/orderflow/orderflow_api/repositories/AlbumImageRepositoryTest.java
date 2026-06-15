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
public class AlbumImageRepositoryTest {

    @Autowired
    private AlbumImageRepository albumImageRepository;

    private AlbumImage albumImageOne;
    private AlbumImage albumImageTwo;
    private AlbumImage albumImageThree;
    private AlbumImage albumImageFour;


    @BeforeEach
    public void setup() {

        albumImageRepository.saveAndFlush(albumImageOne);

        albumImageOne = new AlbumImage(
                "Album Image One",
                "Album Image One Description"
        );
        albumImageTwo = new AlbumImage(
                "Album Image Two",
                "Album Image One Description"
        );
        albumImageThree = new AlbumImage(
                "Album Image Three",
                "Album Image Three Description"
        );
        albumImageFour = new AlbumImage(
                "Album Image Four",
                "Album Image Four Description"
        );

    }

    @DisplayName("JUnit test for Given AlbumImage Object when Save then Return AlbumImage Object")
    @Test
    void testGivenALbumImageObject_whenSave_thenReturnAlbumImageObject(){
        // Given/Arrange

        // When/Act
        AlbumImage savedAlbumImage = albumImageRepository.save(albumImageOne);

        // Then/Assert
        assertNotNull(savedAlbumImage);
        assertTrue(savedAlbumImage.getAlbumImageid()>0);
        assertEquals("Album Image One", savedAlbumImage.getTitle());
        assertEquals("Album Image One Description", savedAlbumImage.getDescription());
    }

    @DisplayName("JUnit test for Given AlbumImage Object when Find By Id then Return Album Image Object")
    @Test
    void testGivenAlbumImageObject_whenFindById_thenReturnAlbumImageObject(){
        // Given/Arrange
        albumImageRepository.save(albumImageOne);
        albumImageRepository.save(albumImageTwo);

        // When/Act
        AlbumImage firstAlbumImage = albumImageRepository.findById(albumImageOne.getAlbumImageid()).get();
        AlbumImage secondAlbumImage = albumImageRepository.findById(albumImageTwo.getAlbumImageid()).get();

        // Then/Assert
        assertNotNull(firstAlbumImage);
        assertNotNull(secondAlbumImage);
        assertEquals(albumImageOne.getAlbumImageid(), firstAlbumImage.getAlbumImageid());
        assertEquals(albumImageTwo.getAlbumImageid(), secondAlbumImage.getAlbumImageid());
    }

    @DisplayName("JUnit test for Given AlbumImage Object when Update AlbumImage then Return AlbumImage Object")
    @Test
    void testGivenAlbumImageObject_whenUpdateAlbumImage_thenReturnUpdateObject(){
        // Given/Arrange
        albumImageRepository.save(albumImageOne);

        // When/Act
        AlbumImage savedAlbumImage = albumImageRepository.findById(albumImageOne.getAlbumImageid()).get();
        savedAlbumImage.setTitle("Changed Title");
        savedAlbumImage.setDescription("Changed Description");

        AlbumImage updatedAlbumImage = albumImageRepository.save(savedAlbumImage);

        // Then/Assert
        assertNotNull(updatedAlbumImage);
        assertEquals("Changed Title", updatedAlbumImage.getTitle());
        assertEquals("Changed Description", updatedAlbumImage.getDescription());
    }

    @DisplayName("JUnit test for Given AlbumImage Object when Delete AlbumImageById then Remove AlbumImage Object")
    @Test
    void testGivenAlbumImageObject_whenDeleteAlbumImageById_thenRemoveAlbumImage(){
        // Given/Arrange
        albumImageRepository.save(albumImageOne);

        // When/Act
        albumImageRepository.deleteById(albumImageOne.getAlbumImageid());
        Optional<AlbumImage> albumImageOptional = albumImageRepository.findById(albumImageOne.getAlbumImageid());

        // Then/Assert
        assertTrue(albumImageOptional.isEmpty());
    }
}