package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.utils.ObjectUtils;
import com.ecommerce.Domain.Application.Exceptions.BadRequest;
import com.ecommerce.Domain.Application.Repositories.UploadFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CloudinaryUploadFileRepository implements UploadFileRepository {

    private Cloudinary cloudinary;

    public CloudinaryUploadFileRepository() {
        this.cloudinary = Singleton.getCloudinary();
    }

    public String uploadImage(MultipartFile imageFile) {
        try {
            return (String) cloudinary.uploader().upload(imageFile.getBytes(),
                    ObjectUtils.asMap("folder", "api-spring",
                            "resource_type","image")).get("url");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequest("Couldn't upload this image..");
        }
    }
   public String overrideImage(MultipartFile imageFile, String url) {

        String[] urlSplited = url.split("/");
        String id = urlSplited[urlSplited.length - 1].split("\\.")[0];

        try {
            return (String) cloudinary.uploader().upload(imageFile.getBytes(),
                    ObjectUtils.asMap(
                            "public_id",id,
                            "folder", "api-spring",
                            "resource_type","image",
                            "overwrite", true)).get("url");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequest("Couldn't upload this image..");
        }
    }


    public String deleteImage(String id) {
        try {
            return (String) cloudinary.uploader().destroy(id,
                    ObjectUtils.asMap("resource_type","image")).get("result");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequest("Couldn't upload this image..");
        }
    }
}
