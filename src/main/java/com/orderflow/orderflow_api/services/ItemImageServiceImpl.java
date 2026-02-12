package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.models.ItemImage;
import com.orderflow.orderflow_api.payload.ItemImageDTO;
import com.orderflow.orderflow_api.payload.ItemImageRequestDTO;
import com.orderflow.orderflow_api.repositories.ItemImageRepository;
import com.orderflow.orderflow_api.repositories.ItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ItemImageServiceImpl implements ItemImageService {

    @Autowired
    private ItemImageRepository itemImageRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Override
    public ItemImageDTO addItemImage(Long itemId, ItemImageRequestDTO itemImageRequestDTO, MultipartFile fileImage) throws IOException {
        Item itemFromDB = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "itemId",itemId));

        String fileName = fileService.uploadImageFile(path, fileImage);

        ItemImage itemImage = modelMapper.map(itemImageRequestDTO, ItemImage.class);
        itemImage.setItem(itemFromDB);
        itemImage.setDescription(itemImageRequestDTO.getDescription());
        itemImage.setTitle(itemImageRequestDTO.getTitle());
        itemImage.setUrl(fileName);

        ItemImage savedItemImage = itemImageRepository.save(itemImage);

        return modelMapper.map(savedItemImage, ItemImageDTO.class);
    }

    @Override
    public ItemImageDTO updateImageFile(Long itemImageId, MultipartFile fileImage) throws IOException {
        ItemImage itemImageFromDB = itemImageRepository.findById(itemImageId)
                .orElseThrow(() -> new ResourceNotFoundException("ItemImage", "itemImageId", itemImageId));

        String fileName = fileService.uploadImageFile(path, fileImage);

        itemImageFromDB.setUrl(fileName);

        ItemImage savedItemImage = itemImageRepository.save(itemImageFromDB);

        return modelMapper.map(savedItemImage, ItemImageDTO.class);
    }

    private String imageUrlContruct(String imageTitle){
        return imageBaseUrl.endsWith("/") ? imageBaseUrl + imageTitle : imageBaseUrl + "/" + imageTitle;
    }
}
