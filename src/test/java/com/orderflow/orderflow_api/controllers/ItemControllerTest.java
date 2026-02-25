package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.payload.ItemDTO;
import com.orderflow.orderflow_api.services.ItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    @Test
    @DisplayName("Should not do nothing on delete case")
    void shouldDeletarItem() throws Exception {
        // ------------ ARRANGE --------------
        Long itemId = 1L;

        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(itemId);
        itemDTO.setQuantity(1);
        itemDTO.setItemName("Item name 1");
        itemDTO.setItemStatus("Enable");
        itemDTO.setItemSize("Normal");
        itemDTO.setPrice(13.99);
        itemDTO.setDiscount(2.00);
        itemDTO.setDescription("Description 1");

        // ------------ ACT --------------
        when(itemService.deleteItem(itemId)).thenReturn(itemDTO);
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/items/{itemId}", itemId))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.itemId").value(itemId))
                .andExpect((ResultMatcher) jsonPath("$.itemQuantity").value(1))
                .andExpect((ResultMatcher) jsonPath("$.itemName").value("Item name 1"))
                .andExpect((ResultMatcher) jsonPath("$.itemStatus").value("Enable"))
                .andExpect((ResultMatcher) jsonPath("$.itemSize").value("Normal"))
                .andExpect((ResultMatcher) jsonPath("$.price").value(13.99))
                .andExpect((ResultMatcher) jsonPath("$.discount").value(2.00))
                .andExpect((ResultMatcher) jsonPath("$.description").value("Description 1"));

        // ------------ ASSERT --------------

        verify(itemService, times(1)).deleteItem(itemId);
    }
}
