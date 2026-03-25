package com.orderflow.orderflow_api.mappers;

import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.payload.ItemCategoryDTO;
import com.orderflow.orderflow_api.payload.ItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDTO toItemDTO(Item item);

    @Bean
    @Mapping(target = "categoryName", source = "category.categoryName")
    ItemCategoryDTO toItemCategoryDTO(Item item);
}
