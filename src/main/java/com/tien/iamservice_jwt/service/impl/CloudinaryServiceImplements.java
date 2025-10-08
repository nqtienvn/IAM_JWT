package com.tien.iamservice_jwt.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tien.iamservice_jwt.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudinaryServiceImplements implements CloudinaryService {
    private final Cloudinary cloudinary;
    @Override
    public String uploadAvatar(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        //get file Name la mot cai MANG
        String publicValue = generatePublicValue(file.getOriginalFilename()); //lay cai ten anh
        String extension = getFileName(file.getOriginalFilename())[1];//lay e cai duoi cua anh
        File fileUpload = convert(file, publicValue, extension); //convert de thanh cai file
        try{
            cloudinary.uploader().upload(
                    fileUpload,
                    ObjectUtils.asMap(
                            "public_id", publicValue,
                            "resource_type", "image"
                    )
            );
            return cloudinary.url().resourceType("image").generate(publicValue + "." + extension);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            cleanDisk(fileUpload);
        }
    }
    private File convert(MultipartFile file, String publicValue, String extension) {
        File convFile = new File(publicValue + "." + extension);
        try(InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, convFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convFile;
    }
    public String generatePublicValue(String originalFilename) {
        String fileName = getFileName(originalFilename)[0];
        return UUID.randomUUID() + "_" + fileName;
    }
    public String[] getFileName(String originalFilename) {
        return originalFilename.split("\\.");
    }
    private void cleanDisk(File file) {
        try {
            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Error deleting file: {}", file.getName(), e);
        }
    }
}
