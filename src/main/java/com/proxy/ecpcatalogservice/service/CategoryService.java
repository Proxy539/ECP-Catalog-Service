package com.proxy.ecpcatalogservice.service;

import com.proxy.ecpcatalogservice.dto.CreateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.CreateCategoryResponse;

public interface CategoryService {

    CreateCategoryResponse createCategory(CreateCategoryRequest createCategoryRequest);
}
