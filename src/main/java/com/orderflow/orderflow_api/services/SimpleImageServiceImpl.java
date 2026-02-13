package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.AlbumImage;
import com.orderflow.orderflow_api.models.SimpleImage;
import com.orderflow.orderflow_api.payload.ItemImageDTO;
import com.orderflow.orderflow_api.payload.ItemImageRequestDTO;
import com.orderflow.orderflow_api.payload.SimpleImageDTO;
import com.orderflow.orderflow_api.payload.SimpleImageRequestDTO;
import com.orderflow.orderflow_api.repositories.AlbumImageRepository;
import com.orderflow.orderflow_api.repositories.SimpleImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SimpleImageServiceImpl implements SimpleImageService {

    @Autowired
    private AlbumImageRepository albumImageRepository;

    @Autowired
    private SimpleImageRepository simpleImageRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${project.image}")
    private String path;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Override
    public SimpleImageDTO addImage(Long albumImageId, SimpleImageRequestDTO simpleImageRequestDTO, MultipartFile file) throws IOException {
        AlbumImage albumImageFromDb = albumImageRepository.findById(albumImageId)
                .orElseThrow(() -> new ResourceNotFoundException("AlbumImage", "albumImageId", albumImageId));

        String urlImage = fileService.uploadImageFile(path, file);

        SimpleImage simpleImage = modelMapper.map(simpleImageRequestDTO, SimpleImage.class);
        simpleImage.setTitle(simpleImageRequestDTO.getTitle());
        simpleImage.setUrl(urlImage);
        simpleImage.setAlbumImage(albumImageFromDb);
        simpleImageRepository.save(simpleImage);

        Set<SimpleImage> images = albumImageFromDb.getSimpleImages();
        images.add(simpleImage);
        albumImageFromDb.setSimpleImages(images);
        albumImageRepository.save(albumImageFromDb);

        return modelMapper.map(simpleImage, SimpleImageDTO.class);
    }

    @Override
    public SimpleImageDTO updateImageFile(Long simpleImageId, MultipartFile file) throws IOException {
        SimpleImage simpleImageFromDb = simpleImageRepository.findById(simpleImageId)
                .orElseThrow(() -> new ResourceNotFoundException("SimpleImage", "simpleImageId", simpleImageId));

        String urlImage = fileService.uploadImageFile(path, file);

        simpleImageFromDb.setUrl(urlImage);
        simpleImageRepository.save(simpleImageFromDb);

        return modelMapper.map(simpleImageFromDb, SimpleImageDTO.class);
    }
}
