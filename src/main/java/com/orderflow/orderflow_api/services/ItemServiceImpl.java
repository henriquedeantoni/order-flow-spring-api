package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.payload.ItemDTO;
import com.orderflow.orderflow_api.payload.ItemResponse;
import com.orderflow.orderflow_api.repositories.CategoryRepository;
import com.orderflow.orderflow_api.repositories.ItemRepository;
import com.orderflow.orderflow_api.security.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;

    @Override
    public ItemResponse getAllItems(String keyword, String category, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder) {
        Sort sortByIdAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByIdAndOrder);

        Specification<Item> specification = Specification.allOf(List.of());

        if(keyword!=null && !keyword.isEmpty()){
            specification = specification.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.like(criteriaBuilder.lower(root.get("itemName")), "%" + keyword.toLowerCase() + "%"));
        }

        if(category!=null && !category.isEmpty()){
            specification = specification.and((root, query, criteriabuilder)
                    -> criteriabuilder.like(root.get("category").get("categoryName"), category));
        }

        Page<Item> itemsPage = itemRepository.findAll(specification, pageDetails);

        List<Item> items = itemsPage.getContent();

        List<ItemDTO> itemDTOS = items
                .stream()
                .map(item ->{
                    ItemDTO itemDTO = modelMapper.map(item, ItemDTO.class);
                    //ItemDTO.setImage();
                    return itemDTO;
                }).toList();
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setContent(itemDTOS);
        itemResponse.setPageNumber(itemsPage.getNumber());
        itemResponse.setPageSize(itemsPage.getSize());
        itemResponse.setTotalPages(itemsPage.getTotalPages());
        itemResponse.setTotalElements(itemsPage.getTotalElements());
        itemResponse.setLastPage(itemsPage.isLast());
        itemResponse.setTimestamp(LocalDateTime.now(ZoneId.of("UTC")));
        return itemResponse;
    }

    @Override
    public ItemDTO addItem(Long categoryId, ItemDTO itemDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        Item item = modelMapper.map(itemDTO, Item.class);
        item.setCategory(category);
        item.setUser(authUtil.userOnLoggedSession());

        Item savedItem = itemRepository.save(item);
        return modelMapper.map(savedItem, ItemDTO.class);
    }

    @Override
    public ItemDTO updateItem(ItemDTO itemDTO, Long itemId) {
        Item itemFromDb = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "ItemId", itemId));

        itemFromDb.setItemSize(itemDTO.getItemSize());
        itemFromDb.setItemName(itemDTO.getItemName());
        itemFromDb.setItemStatus(itemDTO.getItemStatus());
        itemFromDb.setPrice(itemDTO.getPrice());
        itemFromDb.setQuantity(itemDTO.getQuantity());
        itemFromDb.setItemStatus(itemDTO.getItemStatus());
        itemFromDb.setDescription(itemDTO.getDescription());
        itemFromDb.setTimePrepareMinutes(itemDTO.getTimePrepareMinutes());
        itemFromDb.setDiscount(itemDTO.getDiscount());

        Item savedItem = itemRepository.save(itemFromDb);

        return modelMapper.map(savedItem, ItemDTO.class);
    }
}
