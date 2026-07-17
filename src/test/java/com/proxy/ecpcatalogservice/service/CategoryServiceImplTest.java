package com.proxy.ecpcatalogservice.service;

import com.proxy.ecpcatalogservice.dto.CreateCategoryRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryServiceImplTest {

    private static final String TEST_CATEGORY_NAME = "test category name";
    private static final String TEST_CATEGORY_DESCRIPTION = "test category description";

    private final CategoryServiceImpl categoryService = new CategoryServiceImpl();

    @Test
    void givenCreateCategoryRequestWhenCreateCategoryThenReturnCreateCategoryResponse() {

        final var createCategoryRequest = new CreateCategoryRequest(TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);

        final var createCategoryResponse = categoryService.createCategory(createCategoryRequest);

        assertThat(createCategoryResponse.id()).isNotEmpty();
        assertThat(createCategoryResponse.name()).isEqualTo(TEST_CATEGORY_NAME);
        assertThat(createCategoryRequest.description()).isEqualTo(TEST_CATEGORY_DESCRIPTION);
    }


}