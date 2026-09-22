package com.proxy.ecpcatalogservice.service;

import com.proxy.ecpcatalogservice.dto.CreateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.CreateCategoryResponse;
import com.proxy.ecpcatalogservice.dto.GetCategoriesResponse;
import com.proxy.ecpcatalogservice.dto.GetCategoryResponse;
import com.proxy.ecpcatalogservice.dto.UpdateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.UpdateCategoryResponse;
import com.proxy.ecpcatalogservice.exception.ResourceNotFoundException;
import com.proxy.ecpcatalogservice.mapper.CategoryMapper;
import com.proxy.ecpcatalogservice.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
class CategoryServiceImpl implements CategoryService {

    private final static String CATEGORY_NOT_FOUND_MESSAGE = "Category not found by id %s";

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public CreateCategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        final var category = categoryMapper.toCategory(createCategoryRequest);

        final var savedCategory = categoryRepository.save(category);

        return categoryMapper.toCreateCategoryResponse(savedCategory);
    }

    @Override
    public GetCategoryResponse getCategory(UUID id) {

        final var category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND_MESSAGE.formatted(id)));

        return categoryMapper.toGetCategoryResponse(category);

    }

    @Override
    public GetCategoriesResponse getCategories() {
        final var categories = categoryRepository.findAll();

        return categoryMapper.toGetCategoriesResponse(categories);
    }

    @Override
    @Transactional
    public UpdateCategoryResponse updateCategory(UUID id, UpdateCategoryRequest updateCategoryRequest) {
        final var savedCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND_MESSAGE.formatted(id)));

        categoryMapper.updateCategory(savedCategory, updateCategoryRequest);

        return categoryMapper.toUpdateCategoryResponse(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {

        final var savedCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND_MESSAGE.formatted(id)));

        categoryRepository.delete(savedCategory);
    }
}
