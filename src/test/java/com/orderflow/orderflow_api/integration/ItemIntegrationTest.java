package com.orderflow.orderflow_api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.payload.ItemDTO;
import com.orderflow.orderflow_api.repositories.CategoryRepository;
import com.orderflow.orderflow_api.repositories.ItemRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ItemIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create a item from db with success")
    void itemIntegrationTest() throws Exception {

        // ---------- ARRANGE ----------

        Category category = new Category();
        category.setCategoryName("Category 1");
        category = categoryRepository.save(category);

        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemName("Item 1");
        itemDTO.setItemStatus("status 1");
        itemDTO.setItemSize("size 1");
        itemDTO.setPrice(99.99);
        itemDTO.setDiscount(10.00);
        itemDTO.setDescription("Description 1");

        // ---------- ACT ----------

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/items/" + category.getCategoryId() + "/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemName").value("Item 1"))
                .andExpect(jsonPath("$.itemStatus").value("status 1"))
                .andExpect(jsonPath("$.itemSize").value("size 1"))
                .andExpect(jsonPath("$.itemPrice").value(99.99))
                .andExpect(jsonPath("$.itemDiscount").value(10.00))
                .andExpect(jsonPath("$.itemDescription").value("Description 1"));

        // ---------- ASSERT ----------

        assertEquals(1, itemRepository.count());
    }
    
    @Test
    @DisplayName("Should delete a item by hitfrom db with success")
    void shouldDeleteItemById() throws Exception {

        Item item = new Item();
        item.setItemName("Item name 1");
        item.setQuantity(1);
        item.setItemStatus("Enable");
        item.setItemSize("Normal");
        item.setPrice(13.99);
        item.setDiscount(2.00);
        item.setDescription("Description 1");

        item = itemRepository.save(item);

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/items/{itemId}", item.getItemId()))
                .andExpect(status().isNoContent());

        Optional<Item> deletedItem = itemRepository.findById(item.getItemId());

        assertFalse(deletedItem.isPresent(), "Item not found");
    }


}
