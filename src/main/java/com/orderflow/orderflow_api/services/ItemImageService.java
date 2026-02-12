package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.ItemImageDTO;
import com.orderflow.orderflow_api.payload.ItemImageRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ItemImageService {
    ItemImageDTO addItemImage(Long itemId, ItemImageRequestDTO itemImageRequestDTO, MultipartFile file) throws IOException;

    ItemImageDTO updateImageFile(Long itemImageId, MultipartFile file) throws IOException;
}
