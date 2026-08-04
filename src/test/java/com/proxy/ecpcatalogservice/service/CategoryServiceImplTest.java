package com.proxy.ecpcatalogservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import com.proxy.ecpcatalogservice.dto.CreateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.CreateCategoryResponse;
import com.proxy.ecpcatalogservice.dto.GetCategoryResponse;
import com.proxy.ecpcatalogservice.exception.ResourceNotFoundException;
import com.proxy.ecpcatalogservice.mapper.CategoryMapper;
import com.proxy.ecpcatalogservice.model.Category;
import com.proxy.ecpcatalogservice.repository.CategoryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    private static final UUID TEST_CATEGORY_UUID = UUID.randomUUID();
    private static final String TEST_CATEGORY_NAME = "test category name";
    private static final String TEST_CATEGORY_DESCRIPTION = "test category description";

    private final static String CATEGORY_NOT_FOUND_MESSAGE = "Category not found by id %s";

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void givenCategoryNotExistWhenGetCategoryThenThrowNotFoundException() {
        when(categoryRepository.findById(TEST_CATEGORY_UUID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategory(TEST_CATEGORY_UUID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(CATEGORY_NOT_FOUND_MESSAGE.formatted(TEST_CATEGORY_UUID));

        verify(categoryRepository).findById(TEST_CATEGORY_UUID);
    }

    @Test
    void givenCategoryExistsWhenGetCategoryThenReturnCategory() {
        final var category = new Category(TEST_CATEGORY_UUID, TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);
        final var getCategoryResponse = new GetCategoryResponse(TEST_CATEGORY_UUID, TEST_CATEGORY_NAME,
                TEST_CATEGORY_DESCRIPTION);

        when(categoryRepository.findById(TEST_CATEGORY_UUID))
                .thenReturn(Optional.of(category));
        when(categoryMapper.tGetCategoryResponse(category))
                .thenReturn(getCategoryResponse);

        final var response = categoryService.getCategory(TEST_CATEGORY_UUID);

        assertThat(response).isEqualTo(getCategoryResponse);

        verify(categoryRepository).findById(TEST_CATEGORY_UUID);
        verify(categoryMapper).tGetCategoryResponse(category);
    }

    @Test
    void givenCreateCategoryRequestWhenCreateCategoryThenReturnCreateCategoryResponse() {
        final var createCategoryRequest = new CreateCategoryRequest(TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);
        final var category = new Category(TEST_CATEGORY_UUID, TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);
        final var expectedCreateCategoryResponse = new CreateCategoryResponse(TEST_CATEGORY_UUID, TEST_CATEGORY_NAME,
                TEST_CATEGORY_DESCRIPTION);

        when(categoryMapper.toCategory(createCategoryRequest)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCreateCategoryResponse(category)).thenReturn(expectedCreateCategoryResponse);

        final var createCategoryResponse = categoryService.createCategory(createCategoryRequest);

        assertThat(createCategoryResponse).isEqualTo(expectedCreateCategoryResponse);

        verify(categoryMapper).toCategory(createCategoryRequest);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toCreateCategoryResponse(category);
    }

}
