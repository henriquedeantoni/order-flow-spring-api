package com.orderflow.orderflow_api.integration;

import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.repositories.ItemRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ItemIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Test
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
