package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.ItemImageDTO;
import com.orderflow.orderflow_api.payload.ItemImageRequestDTO;
import com.orderflow.orderflow_api.payload.SimpleImageDTO;
import com.orderflow.orderflow_api.payload.SimpleImageRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SimpleImageService {
    SimpleImageDTO addImage(Long albumImageId, SimpleImageRequestDTO simpleImageRequestDTO, MultipartFile file) throws IOException;

    SimpleImageDTO updateImageFile(Long simpleImageId, MultipartFile file) throws IOException;
}
