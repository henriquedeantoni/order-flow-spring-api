package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.models.ItemImage;
import com.orderflow.orderflow_api.payload.ItemImageDTO;
import com.orderflow.orderflow_api.payload.ItemImageRequestDTO;
import com.orderflow.orderflow_api.payload.LocalDTO;
import com.orderflow.orderflow_api.repositories.ItemImageRepository;
import com.orderflow.orderflow_api.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ItemImageServiceTest {

    @InjectMocks
    private ItemImageServiceImpl itemImageService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ItemImageRepository itemImageRepository;

    @Mock
    private ItemRepository itemRepository;

    private Item itemOne;

    private ItemImage itemImageOne;
    private ItemImage itemImageTwo;
    private ItemImage itemImageThree;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        modelMapper = new ModelMapper();

        itemOne = new Item("item name", "item name description", 10, 256.00);

        itemImageOne = new ItemImage("/urlOne", "imageOne", "imageOne Description");
        itemImageTwo = new ItemImage("/urlTwo", "imageTwo", "imageTwo Description");
        itemImageThree = new ItemImage("/urlThree", "imageThree", "imageThree Description");
    }

    @DisplayName("JUnit test for Given ItemImage When Add Item Image Then Return ItemImage DtoObject")
    @Test
    void testGivenItemImageWhenAddItemImageThenReturnItemImageDtoObject() throws IOException {
        // Given/Arrange
        Long itemImageId = 1L;
        given(itemImageRepository.findById(anyLong())).willReturn(Optional.of(itemImageOne));
        given(itemImageRepository.save(any(ItemImage.class))).willReturn(itemImageOne);
        ItemImageDTO itemImageDTO = new ModelMapper().map(itemImageOne, ItemImageDTO.class);
        ItemImageRequestDTO itemImageRequestDTO = new ModelMapper().map(itemImageOne, ItemImageRequestDTO.class);

        MultipartFile file = Mockito.mock(MultipartFile.class);

        // When/Act
        ItemImageDTO savedItemImageDTO = itemImageService.addItemImage(itemImageId, itemImageRequestDTO, file);

        // Then/Assert
        assertNotNull(savedItemImageDTO);
        assertEquals("/urlOne",savedItemImageDTO.getUrl());
        assertEquals("imageOne", savedItemImageDTO.getTitle());

    }

    @DisplayName("JUnit test for Given Item Image When Add Item With Inexistent Id Then Throws Resource Not Found Exception")
    @Test
    void testGivenItemImageWhenAddItemWithInexistentIdThenThrowsResourceNotFoundException() throws IOException {
        // Given/Arrange
        Long validItemId = 1L;
        Long invalidItemId = 2L;

        itemOne.setItemId(validItemId);
        given(itemRepository.findById(1L)).willReturn(Optional.of(itemOne));
        given(itemRepository.findById(2L)).willReturn(Optional.empty());
        ItemImageDTO itemImageDTO = new ModelMapper().map(itemImageOne, ItemImageDTO.class);
        ItemImageRequestDTO itemImageRequestDTO = new ModelMapper().map(itemImageOne, ItemImageRequestDTO.class);

        MultipartFile file = Mockito.mock(MultipartFile.class);

        // When/Act
        assertThrows( ResourceNotFoundException.class, ()->{
            itemImageService.addItemImage(2L, itemImageRequestDTO, file);
        });

        // Then/Assert
        verify(itemRepository).findById(invalidItemId);
    }

    //ItemImageDTO updateImageFile(Long itemImageId, MultipartFile fileImage)

    @DisplayName("Junit test for Given item image object when updated item image then return item imageDTO object")
    @Test
    void testGivenItemImageObjectWhenUpdatedItemImageThenReturnItemImageDTOobject() throws IOException {
        // Given/Arrange
        Long itemImageId = 1L;
        Long invalidItemId = 2L;
        itemImageOne.setItem(itemOne);

        given(itemRepository.findById(1L)).willReturn(Optional.of(itemOne));
        given(itemRepository.findById(2L)).willReturn(Optional.empty());
        ItemImageDTO itemImageDTO = new ModelMapper().map(itemImageOne, ItemImageDTO.class);
        ItemImageRequestDTO itemImageRequestDTO = new ModelMapper().map(itemImageOne, ItemImageRequestDTO.class);

        MultipartFile file = Mockito.mock(MultipartFile.class);

        // When/Act

        ItemImageDTO savedItemImageDTO = itemImageService.updateImageFile(1L, file);

        // Then/Assert
    }

    @DisplayName("Junit test for Given item image object when updated item image With ItemId Inexistent then return item imageDTO object")
    @Test
    void testGivenItemImageObjectWhenUpdatedItemImageWithItemIdInexitentThenReturnItemImageDTOobject() throws IOException {
        // Given/Arrange
        Long itemImageId = 1L;

        // When/Act


        // Then/Assert
    }

}
