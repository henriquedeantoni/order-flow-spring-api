package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.models.AlbumImage;
import com.orderflow.orderflow_api.payload.AlbumImageDTO;
import com.orderflow.orderflow_api.services.AlbumImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1")
public class AlbumImageController {
    @Autowired
    private AlbumImageService albumImageService;

    @PostMapping("/admin/album-images/{itemId}/images")
    public ResponseEntity<AlbumImageDTO> createAlbumImage(
            @RequestBody AlbumImageDTO albumImageDTO,
            @RequestParam Long itemId) {
        AlbumImageDTO savedAlbumImageDTO = albumImageService.addAlbum(albumImageDTO, itemId);

        return new ResponseEntity<>(savedAlbumImageDTO, HttpStatus.CREATED);
    }

    @PostMapping("/admin/album-images/{albumImageId}/information")
    public ResponseEntity<AlbumImageDTO> updateAlbumImageInformation(
            @RequestBody AlbumImageDTO albumImageDTO,
            @PathVariable Long albumImageId){
        AlbumImageDTO updatedAlbumImageDTO = albumImageService.updateAlbumImageInfo(albumImageDTO, albumImageId);
        return new ResponseEntity<>(updatedAlbumImageDTO, HttpStatus.OK);
    }

    @PostMapping("/admin/album-images/{albumImageId}/image/{urlImage}")
    public ResponseEntity<AlbumImageDTO> updateAlbumImageInfoAndFile(
            @RequestBody AlbumImageDTO albumImageDTO,
            @PathVariable Long albumImageId,
            @RequestParam("image") MultipartFile image,
            @PathVariable String urlImage) throws IOException {
        AlbumImageDTO updatedAlbumImageDTO = albumImageService.updateAlbumImageInfoAndFile(albumImageDTO, albumImageId, image, urlImage);
        return new ResponseEntity<>(updatedAlbumImageDTO, HttpStatus.OK);
    }
}
