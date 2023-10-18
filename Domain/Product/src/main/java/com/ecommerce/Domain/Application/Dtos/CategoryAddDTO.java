package com.ecommerce.Domain.Application.Dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryAddDTO {
    @NotEmpty
    private String name;

    @NotEmpty
    private MultipartFile img;

    private Boolean state;
}
