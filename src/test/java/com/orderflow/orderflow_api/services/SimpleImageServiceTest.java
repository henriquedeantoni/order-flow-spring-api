package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.AlbumImage;
import com.orderflow.orderflow_api.models.SimpleImage;
import com.orderflow.orderflow_api.models.User;
import com.orderflow.orderflow_api.payload.SimpleImageDTO;
import com.orderflow.orderflow_api.payload.SimpleImageRequestDTO;
import com.orderflow.orderflow_api.repositories.AlbumImageRepository;
import com.orderflow.orderflow_api.repositories.SimpleImageRepository;
import com.orderflow.orderflow_api.security.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SimpleImageServiceTest {

    private ModelMapper modelMapper;

    @Mock
    private SimpleImageRepository simpleImageRepository;

    @Mock
    private AlbumImageRepository albumImageRepository;

    @Mock
    private FileServiceImpl fileServiceImpl;

    @Mock
    private AuthUtil authUtil;

    @InjectMocks
    private SimpleImageServiceImpl simpleImageService;

    private SimpleImage simpleImageOne;
    private SimpleImage simpleImageTwo;
    private SimpleImage simpleImageThree;
    private SimpleImage simpleImageFour;
    private SimpleImage simpleImageFive;

    private AlbumImage albumImageOne;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        modelMapper = new ModelMapper();

        ReflectionTestUtils.setField(simpleImageService, "modelMapper", new ModelMapper());

        simpleImageOne = new SimpleImage("/6e0d7690-9df9-4de6-aa6f-dc52bf39b96f","Image One");
        simpleImageTwo = new SimpleImage("/811e1bcf-7264-4284-9a84-658cc5de0884", "Image Two");
        simpleImageThree = new SimpleImage("/f018f32e-538d-411e-96e5-c52288b3f203", "Image Three");
        simpleImageFour = new SimpleImage("/7b46ebd5-dc03-4a27-8420-f8ad39e3b270", "Image Four");
        simpleImageFive = new SimpleImage("/528e32f6-5f51-4bc5-8bc8-98eec4af5d46", "Image Five");

        albumImageOne = new AlbumImage("album image title", "album image description");
    }

    @DisplayName("JUnit test for Given Simple Image Object When Add Image Then Return Simple Image Dto Object")
    @Test
    void testGivenSimpleImageObjectWhenAddImageThenReturnSimpleImageDtoObject() throws IOException {
        // Given/Arrange
        Long albumImageId = 1L;
        given(authUtil.userOnLoggedSession()).willReturn(new User("username", "user@email.com", "hashPass", "firstName", "lastName"));
        given(simpleImageRepository.findById(anyLong())).willReturn(Optional.of(simpleImageOne));
        given(albumImageRepository.findById(anyLong())).willReturn(Optional.of(albumImageOne));
        given(simpleImageRepository.save(any(SimpleImage.class))).willReturn(simpleImageOne);
        given(fileServiceImpl.uploadImageFile(any(String.class), any(MultipartFile.class))).willReturn("/6e0d7690-9df9-4de6-aa6f-dc52bf39b96f");

        SimpleImageRequestDTO simpleImageRequest = new SimpleImageRequestDTO("Image One");

        MultipartFile file = Mockito.mock(MultipartFile.class);

        // When/Act
        SimpleImageDTO simpleImageDTO = simpleImageService.addImage(albumImageId, simpleImageRequest, file );

        // Then/Assert

        assertNotNull(simpleImageDTO);
        assertEquals("Image One", simpleImageDTO.getTitle());
    }

    @DisplayName("JUnit test for Given Simple Image Object When Add Image With Id Existent Then Throws API Exception")
    @Test
    void testGivenSimpleImageObjectWhenAddImageWithIdExistentThenThrowsAPIException() throws IOException {
        // Given/Arrange
        Long albumImageId = 1L;
        MultipartFile file = Mockito.mock(MultipartFile.class);


        given(simpleImageRepository.findById(1L)).willReturn(Optional.of(simpleImageOne));
        given(albumImageRepository.findById(anyLong())).willReturn(Optional.of(albumImageOne));
        given(simpleImageRepository.save(simpleImageOne)).willReturn(simpleImageOne);
        given(fileServiceImpl.uploadImageFile(anyString(), any(MultipartFile.class))).willReturn("/6e0d7690-9df9-4de6-aa6f-dc52bf39b96f");
        SimpleImageDTO simpleImageDTO = new ModelMapper().map(simpleImageOne, SimpleImageDTO.class);



        // When/Act
        when(file.getOriginalFilename()).thenReturn("image.png");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("content".getBytes()));
        SimpleImageDTO response = simpleImageService.updateImageFile(1L, file);

        // Then/Assert
        assertNotNull(response);
        verify(fileServiceImpl).uploadImageFile(anyString(), any(MultipartFile.class));
        assertEquals("/6e0d7690-9df9-4de6-aa6f-dc52bf39b96f", response.getUrl());
    }




}
