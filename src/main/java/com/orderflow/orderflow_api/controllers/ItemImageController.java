package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.models.ItemImage;
import com.orderflow.orderflow_api.payload.ItemImageDTO;
import com.orderflow.orderflow_api.payload.ItemImageRequestDTO;
import com.orderflow.orderflow_api.services.ItemImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
public class ItemImageController {
    @Autowired
    private ItemImageService itemImageService;

    @PostMapping("/admin/images/{itemId}/image")
    public ResponseEntity<ItemImageDTO> addImage(
            @PathVariable Long itemId,
            @RequestBody ItemImageRequestDTO itemImageRequestDTO,
            @RequestParam("file") MultipartFile file) {
        ItemImageDTO itemImageDTO = itemImageService.addItemImage(itemId, itemImageRequestDTO, file);
        return new ResponseEntity<>(itemImageDTO,  HttpStatus.CREATED);
    }
}
