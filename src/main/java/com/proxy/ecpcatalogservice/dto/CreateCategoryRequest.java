package com.proxy.ecpcatalogservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(

        @NotBlank(message = "Category name should not be blank")
        @Size(max = 100, message = "Category name should be max 100 characters long")
        String name,
        @NotBlank(message = "Category description should not be blank")
        @Size(max = 500, message = "Category description should be max 500 characters long")
        String description

) {
}
