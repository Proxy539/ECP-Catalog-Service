package com.proxy.ecpcatalogservice.mapper;

import com.proxy.ecpcatalogservice.dto.CreateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.CreateCategoryResponse;
import com.proxy.ecpcatalogservice.dto.GetCategoryResponse;
import com.proxy.ecpcatalogservice.model.Category;

import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toCategory(CreateCategoryRequest createCategoryRequest) {
        final var name = createCategoryRequest.name();
        final var description = createCategoryRequest.description();

        return new Category(name, description);
    }

    public CreateCategoryResponse toCreateCategoryResponse(Category category) {
        final var id = category.getId();
        final var name = category.getName();
        final var description = category.getDescription();

        return new CreateCategoryResponse(id, name, description);
    }

    public GetCategoryResponse tGetCategoryResponse(Category category) {
        final var id = category.getId();
        final var name = category.getName();
        final var description = category.getDescription();

        return new GetCategoryResponse(id, name, description);
    }

}
