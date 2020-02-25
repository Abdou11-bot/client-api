package com.project.clientapi.services;

import com.project.clientapi.repositories.image.ImageModel;
import com.project.clientapi.repositories.image.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ImageService {
    private final ImageRepository imageRepository;
    public ImageService(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }
    public void addImage(ImageModel image){
        this.imageRepository.save(image);
    }
    public String getImage(Long id){
        ImageModel image = this.imageRepository.findById(id).orElseThrow();
        return image.getPath();
    }
    public String getImageByClientId(Long id){
        ImageModel image = this.imageRepository.findFirstByClientId(id);
        if(!image.equals(null))
            return image.getPath();
        else
            return "L'image n'existe pas";
    }
    public void deleteImages(Long id){
        this.imageRepository.deleteAllByClientIdEquals(id);
    }
}
