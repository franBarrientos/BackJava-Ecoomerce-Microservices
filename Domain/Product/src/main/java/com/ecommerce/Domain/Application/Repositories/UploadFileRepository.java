package com.ecommerce.Domain.Application.Repositories;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileRepository {
    String uploadImage(MultipartFile imageFile);
    String overrideImage(MultipartFile imageFile, String url);
    String deleteImage(String id);
}
