package com.proxy.ecpcatalogservice.service;

import com.proxy.ecpcatalogservice.dto.CreateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.CreateCategoryResponse;
import com.proxy.ecpcatalogservice.model.Category;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
class CategoryServiceImpl implements CategoryService {

    private final Map<String, Category> categoryMap = new HashMap<>();

    @Override
    public CreateCategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {

        final var uuid = UUID.randomUUID().toString();
        final var category = toCategory(createCategoryRequest, uuid);

        categoryMap.putIfAbsent(uuid, category);

        return toCreateCategoryResponse(category);
    }

    private Category toCategory(CreateCategoryRequest createCategoryRequest, String uuid) {
        final var name = createCategoryRequest.name();
        final var description = createCategoryRequest.description();

        return new Category(uuid, name, description);
    }

    private CreateCategoryResponse toCreateCategoryResponse(Category category) {
        final var uuid = category.id();
        final var name = category.name();
        final var description = category.description();

        return new CreateCategoryResponse(uuid, name, description);
    }
}
