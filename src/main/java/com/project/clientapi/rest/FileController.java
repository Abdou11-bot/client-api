package com.project.clientapi.rest;

import com.project.clientapi.repositories.client.Client;
import com.project.clientapi.repositories.image.*;
import com.project.clientapi.services.ClientService;
import com.project.clientapi.services.FileStorageService;
import com.project.clientapi.services.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/clients/file")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final ImageService imageService;
    private final ClientService clientService;
    @Autowired
    private FileStorageService fileStorageService;
    private ImageModel image;

    public FileController(ImageService imageService, ClientService clientService) {
        this.imageService = imageService;
        this.clientService = clientService;
    }

    @GetMapping("/load/{id}")
    public String getPath(@PathVariable Long id) {
        return this.imageService.getImage(id);
    }

    @GetMapping("/loadClientImage/{id}")
    public String getClientImagePath(@PathVariable Long id) {
        return this.imageService.getImageByClientId(id);
    }

    @PostMapping("/uploadFile/{id}")
    public String uploadFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

        String fileName = fileStorageService.storeFile(file,id);
        /*String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
        */
        image = new ImageModel();
        image.setName(file.getOriginalFilename());
        image.setClientId(id);
        image.setPath("http://localhost:8081/"+id+"/" + file.getOriginalFilename());
        imageService.addImage(image);
        Client client =this.clientService.getClient(id);
        client.setImageSrc(getClientImagePath(id));
        this.clientService.addClient(client);
        return image.getPath();
    }
/*
    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }
*/
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
