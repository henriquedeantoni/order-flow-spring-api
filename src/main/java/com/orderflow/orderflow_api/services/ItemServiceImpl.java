package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.payload.ItemResponse;
import com.orderflow.orderflow_api.repositories.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public ItemResponse getAllItems() {

        List<Item> items = itemRepository.findAll();

        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setContent(items);
        return itemResponse;
    }



}
