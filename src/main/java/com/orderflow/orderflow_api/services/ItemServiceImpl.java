package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.payload.ItemResponse;
import com.orderflow.orderflow_api.repositories.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public ItemResponse getAllItems(String keyword, String category) {

        Specification<Item> specification = Specification.allOf(List.of());

        if(keyword!=null && !keyword.isEmpty()){
            specification = specification.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.like(criteriaBuilder.lower(root.get("itemName")), "%" + keyword.toLowerCase() + "%"));
        }

        if(category!=null && !category.isEmpty()){
            specification = specification.and((root, query, criteriabuilder)
                    -> criteriabuilder.like(root.get("category").get("categoryName"), category));
        }

        List<Item> items = itemRepository.findAll(specification);

        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setContent(items);
        return itemResponse;
    }



}
