package com.proxy.ecpcatalogservice.service;

import java.util.UUID;

import com.proxy.ecpcatalogservice.dto.CreateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.CreateCategoryResponse;
import com.proxy.ecpcatalogservice.dto.GetCategoriesResponse;
import com.proxy.ecpcatalogservice.dto.GetCategoryResponse;

public interface CategoryService {

    CreateCategoryResponse createCategory(CreateCategoryRequest createCategoryRequest);

    GetCategoryResponse getCategory(UUID id);

    GetCategoriesResponse getCategories();
}
