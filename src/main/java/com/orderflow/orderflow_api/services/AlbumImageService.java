package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.AlbumImageDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface AlbumImageService {
    AlbumImageDTO addAlbum(AlbumImageDTO albumImageDTO, Long itemId);

    AlbumImageDTO updateAlbumImageInfo(AlbumImageDTO albumImageDTO, Long albumImageId);

    AlbumImageDTO updateAlbumImageInfoAndFile(AlbumImageDTO albumImageDTO, Long albumImageId, MultipartFile image, String urlImage) throws IOException;
}
