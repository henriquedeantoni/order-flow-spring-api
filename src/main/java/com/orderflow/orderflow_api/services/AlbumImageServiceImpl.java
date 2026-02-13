package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.AlbumImage;
import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.models.SimpleImage;
import com.orderflow.orderflow_api.payload.AlbumImageDTO;
import com.orderflow.orderflow_api.repositories.AlbumImageRepository;
import com.orderflow.orderflow_api.repositories.ItemRepository;
import com.orderflow.orderflow_api.repositories.SimpleImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AlbumImageServiceImpl implements AlbumImageService {
    @Autowired
    private AlbumImageRepository albumImageRepository;

    @Autowired
    private SimpleImageRepository simpleImageRepository;

    @Autowired
    private SimpleImageService simpleImageService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${project.image}")
    private String path;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Override
    public AlbumImageDTO addAlbum(AlbumImageDTO albumImageDTO, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId));

        AlbumImage albumImage = modelMapper.map(albumImageDTO, AlbumImage.class);
        albumImage.setItem(item);
        albumImageRepository.save(albumImage);

        return modelMapper.map(albumImage, AlbumImageDTO.class);
    }

    @Override
    public AlbumImageDTO updateAlbumImageInfo(AlbumImageDTO albumImageDTO, Long albumImageId) {
        AlbumImage albumImageFromDB = albumImageRepository.findById(albumImageId)
                .orElseThrow(() -> new ResourceNotFoundException("AlbumImage", "albumImageId", albumImageId));

        AlbumImage albumImage = modelMapper.map(albumImageDTO, AlbumImage.class);

        albumImageFromDB.setDescription(albumImage.getDescription());
        albumImageFromDB.setTitle(albumImage.getTitle());

        AlbumImage updatedAlbumImage = albumImageRepository.save(albumImageFromDB);
        return modelMapper.map(updatedAlbumImage, AlbumImageDTO.class);
    }

    @Override
    public AlbumImageDTO updateAlbumImageInfoAndFile(AlbumImageDTO albumImageDTO, Long albumImageId, MultipartFile image, String urlImage) throws IOException {
        AlbumImage albumImageFromDB = albumImageRepository.findById(albumImageId)
                .orElseThrow(() -> new ResourceNotFoundException("AlbumImage", "albumImageId", albumImageId));

        AlbumImage albumImage = modelMapper.map(albumImageDTO, AlbumImage.class);

        albumImageFromDB.setDescription(albumImage.getDescription());
        albumImageFromDB.setTitle(albumImage.getTitle());

        Set<SimpleImage> imagesList = new HashSet<>();
        try{
            imagesList = albumImageFromDB.getSimpleImages();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        if(imagesList.isEmpty())
            throw new APIException("This album has any images");
        else
        {
            for (SimpleImage simpleImage : imagesList) {
                if(simpleImage.getTitle().equals(urlImage)){
                    simpleImageService.updateImageFile(simpleImage.getSimpleImageId(), image);
                    simpleImageRepository.save(simpleImage);
                }
            }
        }

        albumImageRepository.save(albumImageFromDB);
        return modelMapper.map(albumImageFromDB, AlbumImageDTO.class);
    }

}
