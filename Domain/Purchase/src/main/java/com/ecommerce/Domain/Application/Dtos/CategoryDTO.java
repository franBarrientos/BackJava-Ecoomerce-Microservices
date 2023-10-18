package com.ecommerce.Domain.Application.Dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String img;

    private Boolean state;

}
