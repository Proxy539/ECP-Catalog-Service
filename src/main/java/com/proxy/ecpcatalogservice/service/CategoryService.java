package com.proxy.ecpcatalogservice.service;

import java.util.UUID;

import com.proxy.ecpcatalogservice.dto.CreateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.CreateCategoryResponse;
import com.proxy.ecpcatalogservice.dto.GetCategoriesResponse;
import com.proxy.ecpcatalogservice.dto.GetCategoryResponse;
import com.proxy.ecpcatalogservice.dto.UpdateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.UpdateCategoryResponse;
import jakarta.validation.Valid;

public interface CategoryService {

    CreateCategoryResponse createCategory(CreateCategoryRequest createCategoryRequest);

    GetCategoryResponse getCategory(UUID id);

    GetCategoriesResponse getCategories();

    UpdateCategoryResponse updateCategory(UUID categoryUUID, UpdateCategoryRequest updateCategoryRequest);

    void deleteCategory(UUID id);
}
