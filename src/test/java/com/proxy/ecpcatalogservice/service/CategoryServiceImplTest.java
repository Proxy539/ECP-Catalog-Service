package com.proxy.ecpcatalogservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.proxy.ecpcatalogservice.dto.CreateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.CreateCategoryResponse;
import com.proxy.ecpcatalogservice.mapper.CategoryMapper;
import com.proxy.ecpcatalogservice.model.Category;
import com.proxy.ecpcatalogservice.repository.CategoryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    private static final String TEST_CATEGORY_ID = "test category Id";
    private static final String TEST_CATEGORY_NAME = "test category name";
    private static final String TEST_CATEGORY_DESCRIPTION = "test category description";

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;
    
    @Test
    void givenCreateCategoryRequestWhenCreateCategoryThenReturnCreateCategoryResponse() {
        final var createCategoryRequest = new CreateCategoryRequest(TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);
        final var category = new Category(TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);
        final var expectedCreateCategoryResponse = new CreateCategoryResponse(TEST_CATEGORY_ID, TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);

        Mockito.when(categoryMapper.toCategory(createCategoryRequest)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.toCreateCategoryResponse(category)).thenReturn(expectedCreateCategoryResponse);

        final var createCategoryResponse = categoryService.createCategory(createCategoryRequest);

        assertThat(createCategoryResponse).isEqualTo(expectedCreateCategoryResponse);

        verify(categoryMapper).toCategory(createCategoryRequest);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toCreateCategoryResponse(category);
    }


}
