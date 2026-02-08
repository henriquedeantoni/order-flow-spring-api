package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.payload.ItemDTO;
import com.orderflow.orderflow_api.payload.ItemResponse;
import com.orderflow.orderflow_api.repositories.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Autowired
    ModelMapper modelMapper;

    @Mock
    ItemRepository itemRepository;
    @InjectMocks
    ItemService itemService;

    @Test
    @DisplayName("Should list all item with success")
    void shouldReturnPagedItemsSuccessfully() {

        // ------------ ARRANGE --------------
        String keyword = "rice";
        String category = "grains";
        Integer pageNumber = 0;
        Integer pageSize = 2;
        String sortBy = "itemName";
        String sortOrder = "asc";

        // fake item
        Item item = new Item();
        item.setItemId(1l);
        item.setItemName("rice bowl");
        item.setItemStatus("active");
        item.setItemSize("normal");
        item.setTimePrepareMinutes(35);
        item.setDescription("Bowl of baked rice and spicy");
        item.setQuantity(1);

        List<Item> itemList = List.of(item);

        Page<Item> page = new PageImpl<>(itemList, PageRequest.of(pageNumber, pageSize), 1);

        ItemDTO itemDTO = new ItemDTO();
        itemDTO = modelMapper.map(item, ItemDTO.class);

        when(itemRepository.findAll(
                any(Specification.class),
                any(Pageable.class)
        )).thenReturn(page);

        when(modelMapper.map(item, ItemDTO.class)).thenReturn(itemDTO);

        // ------------ ACT --------------

        ItemResponse itemResponse = itemService.getAllItems(
                keyword,
                category,
                pageNumber,
                pageSize,
                sortBy,
                sortOrder);

        // ------------ ASSERT --------------

        assertNotNull(itemResponse);
        assertEquals(1, itemResponse.getContent().size());
        assertEquals(1L, itemResponse.getTotalElements());
        assertEquals(1, itemResponse.getTotalPages());
        assertEquals(0, itemResponse.getPageNumber());
        assertEquals(2, itemResponse.getPageSize());
        assertTrue(itemResponse.isLastPage());

        ItemDTO resulItemDTO = itemResponse.getContent().get(0);
        assertEquals("rice bowl", resulItemDTO.getItemName());

        verify(itemRepository, times(1))
                .findAll(any(Specification.class), any(Pageable.class));

        verify(modelMapper, times(1)).map(item, ItemDTO.class);
    }
}
