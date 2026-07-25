package com.orderflow.orderflow_api.services;

import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class FileServiceTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @DisplayName("JUnit test for Given Path And MultipartFile when upload File Then Return File Name")
    @Test
    void testGivenPathAndMultipartFileWhenUploadFileThenReturnFileName() throws IOException {
        // Given / Arrange
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("image.png");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("content".getBytes()));

        String path = "src/images";

        // When / Act
        String result = fileService.uploadImageFile(path, file);

        // Then / Assert
        assertNotNull(result);
        assertTrue(result.endsWith(".png"));
    }
}
