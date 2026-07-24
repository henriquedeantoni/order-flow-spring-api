package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.ItemImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItemImageServiceTest {

    @Mock
    private ItemService itemService;

    private ItemImage itemImageOne;
    private ItemImage itemImageTwo;
    private ItemImage itemImageThree;

    @BeforeEach
    public void setUp()
    {
        itemImageOne = new ItemImage("/urlOne", "imageOne", "imageOne Description");
        itemImageTwo = new ItemImage("/urlTwo", "imageTwo", "imageTwo Description");
        itemImageThree = new ItemImage("/urlThree", "imageThree", "imageThree Description");
    }

    @DisplayName("JUnit test for Given")
    @Test
    void testGivenItemImageWhenAddItemImageThenReturnItemImageDto()
    {
        // Given/Arrange


        // When/Act


        // Then/Assert


    }

}
