package com.orderflow.orderflow_api.config;

import com.orderflow.orderflow_api.mappers.ItemMapper;
import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.payload.ItemCategoryDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        //mapping to typemap between Item and ItemCategoryDTO
        TypeMap<Item, ItemCategoryDTO> typeMap = modelMapper.createTypeMap(Item.class, ItemCategoryDTO.class);

        //custom mapping
        typeMap.addMapping(src -> src.getCategory().getCategoryName(), ItemCategoryDTO::setCategoryName);

        return modelMapper;
    }
}
