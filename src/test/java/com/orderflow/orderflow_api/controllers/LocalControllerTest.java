package com.orderflow.orderflow_api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderflow.orderflow_api.payload.LocalDTO;
import com.orderflow.orderflow_api.services.LocalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocalController.class)
public class LocalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LocalService localService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create a new Local with success")
    void shouldCreateNewLocalWithSuccess() throws Exception {
        LocalDTO localDTO = new LocalDTO();
        localDTO.setCity("City name");
        localDTO.setCountry("Country name");
        localDTO.setState("State");
        localDTO.setBuildingName("Building name");
        localDTO.setPostalCode("Postal code");
        localDTO.setStreetName("Street name");

        when(localService.registerLocal(any(LocalDTO.class)))
                .thenReturn(localDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/public/locals")
                    .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                    .content(objectMapper.writeValueAsString(localDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.city").value("City name"))
                .andExpect(jsonPath("$.country").value("Country name"))
                .andExpect(jsonPath("$.state").value("State"))
                .andExpect(jsonPath("$.buildingName").value("Building name"))
                .andExpect(jsonPath("$.postalCode").value("Postal code"))
                .andExpect(jsonPath("$.streetName").value("Street name"));
    }
}
