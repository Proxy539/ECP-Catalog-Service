package com.proxy.ecpcatalogservice.service;

import com.proxy.ecpcatalogservice.dto.CreateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.CreateCategoryResponse;
import com.proxy.ecpcatalogservice.mapper.CategoryMapper;
import com.proxy.ecpcatalogservice.repository.CategoryRepository;

import org.springframework.stereotype.Service;

@Service
class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CreateCategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        final var category = categoryMapper.toCategory(createCategoryRequest);

        final var savedCategory = categoryRepository.save(category);

        return categoryMapper.toCreateCategoryResponse(savedCategory);
    }

}
