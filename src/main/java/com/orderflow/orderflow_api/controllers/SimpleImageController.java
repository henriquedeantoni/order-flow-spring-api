package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.payload.ItemImageDTO;
import com.orderflow.orderflow_api.payload.ItemImageRequestDTO;
import com.orderflow.orderflow_api.payload.SimpleImageDTO;
import com.orderflow.orderflow_api.payload.SimpleImageRequestDTO;
import com.orderflow.orderflow_api.services.ItemImageService;
import com.orderflow.orderflow_api.services.SimpleImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/v1")
public class SimpleImageController {
    @Autowired
    private SimpleImageService simpleImageService;

    @PostMapping("/admin/simple-images/{albumImageId}/image")
    public ResponseEntity<SimpleImageDTO> addImage(
            @PathVariable Long albumImageId,
            @RequestBody SimpleImageRequestDTO simpleImageRequestDTO,
            @RequestParam("file") MultipartFile file) throws IOException {
        SimpleImageDTO simpleImageDTO = simpleImageService.addItemImage(albumImageId, simpleImageRequestDTO, file);
        return new ResponseEntity<>(simpleImageDTO,  HttpStatus.CREATED);
    }

    @PutMapping("/admin/simple-images/{simpleImageId}")
    public ResponseEntity<SimpleImageDTO> updateImageFile(
            @PathVariable Long simpleImageId,
            @RequestParam("file") MultipartFile file) throws IOException {
        SimpleImageDTO simpleImageDTO = simpleImageService.updateImageFile(simpleImageId, file);
        return new ResponseEntity<>(simpleImageDTO,  HttpStatus.CREATED);
    }

}
