package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.ItemImageDTO;
import com.orderflow.orderflow_api.payload.ItemImageRequestDTO;
import com.orderflow.orderflow_api.payload.SimpleImageDTO;
import com.orderflow.orderflow_api.payload.SimpleImageRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface SimpleImageService {
    SimpleImageDTO addItemImage(Long itemId, SimpleImageRequestDTO simpleImageRequestDTO, MultipartFile file);

    SimpleImageDTO updateImageFile(Long simpleImageId, MultipartFile file);
}
