package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.ItemImageDTO;
import com.orderflow.orderflow_api.payload.ItemImageRequestDTO;
import com.orderflow.orderflow_api.payload.SimpleImageDTO;
import com.orderflow.orderflow_api.payload.SimpleImageRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SimpleImageServiceImpl implements SimpleImageService {

    @Override
    public SimpleImageDTO addItemImage(Long itemId, SimpleImageRequestDTO simpleImageRequestDTO, MultipartFile file) {
        return null;
    }

    @Override
    public SimpleImageDTO updateImageFile(Long simpleImageId, MultipartFile file) {
        return null;
    }
}
