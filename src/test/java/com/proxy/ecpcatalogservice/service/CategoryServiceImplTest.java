package com.proxy.ecpcatalogservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.proxy.ecpcatalogservice.dto.CreateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.CreateCategoryResponse;
import com.proxy.ecpcatalogservice.dto.GetCategoriesResponse;
import com.proxy.ecpcatalogservice.dto.GetCategoryResponse;
import com.proxy.ecpcatalogservice.dto.UpdateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.UpdateCategoryResponse;
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
    private static final String UPDATE_CATEGORY_NAME = "update category name";
    private static final String UPDATE_CATEGORY_DESCRIPTION = "update category description";

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
        when(categoryMapper.toGetCategoryResponse(category))
                .thenReturn(getCategoryResponse);

        final var response = categoryService.getCategory(TEST_CATEGORY_UUID);

        assertThat(response).isEqualTo(getCategoryResponse);

        verify(categoryRepository).findById(TEST_CATEGORY_UUID);
        verify(categoryMapper).toGetCategoryResponse(category);
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

    @Test
    void givenCategoriesExistWhenGetCategoriesThenReturnGetCategoriesResponse() {
        final var category = new Category(TEST_CATEGORY_UUID, TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);
        final var categories = List.of(category);
        final var getCategoryResponse = new GetCategoryResponse(TEST_CATEGORY_UUID, TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);
        final var getCategoriesList = List.of(getCategoryResponse);
        final var getCategoriesResponse = new GetCategoriesResponse(getCategoriesList);

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toGetCategoriesResponse(categories)).thenReturn(getCategoriesResponse);

        final var result = categoryService.getCategories();

        assertThat(result).isEqualTo(getCategoriesResponse);

        verify(categoryRepository).findAll();
        verify(categoryMapper).toGetCategoriesResponse(categories);
    }

    @Test
    void givenCategoryNotExistsWhenUpdateCategoryThenReturnNotFoundException() {
        final var updateCategoryRequest = new UpdateCategoryRequest(UPDATE_CATEGORY_NAME, UPDATE_CATEGORY_DESCRIPTION);

        when(categoryRepository.findById(TEST_CATEGORY_UUID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.updateCategory(TEST_CATEGORY_UUID, updateCategoryRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(CATEGORY_NOT_FOUND_MESSAGE.formatted(TEST_CATEGORY_UUID));

        verify(categoryRepository).findById(TEST_CATEGORY_UUID);
    }

    @Test
    void givenCategoryExitsWhenUpdateCategoryThenReturnUpdatedCategoryResponse() {
        final var updateCategoryRequest = new UpdateCategoryRequest(UPDATE_CATEGORY_NAME, UPDATE_CATEGORY_DESCRIPTION);
        final var savedCategory = new Category(TEST_CATEGORY_UUID, TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);
        final var updatedCategory = new Category(TEST_CATEGORY_UUID, UPDATE_CATEGORY_NAME, UPDATE_CATEGORY_DESCRIPTION);
        final var updateCategoryResponse = new UpdateCategoryResponse(TEST_CATEGORY_UUID, UPDATE_CATEGORY_NAME, UPDATE_CATEGORY_DESCRIPTION);

        when(categoryRepository.findById(TEST_CATEGORY_UUID))
                .thenReturn(Optional.of(savedCategory));
        when(categoryRepository.save(savedCategory))
                .thenReturn(updatedCategory);
        when(categoryMapper.toUpdateCategoryResponse(updatedCategory))
                .thenReturn(updateCategoryResponse);

        final var result = categoryService.updateCategory(TEST_CATEGORY_UUID, updateCategoryRequest);

        assertThat(result).isEqualTo(updateCategoryResponse);

        verify(categoryRepository).findById(TEST_CATEGORY_UUID);
        verify(categoryMapper).updateCategory(savedCategory, updateCategoryRequest);
        verify(categoryRepository).save(savedCategory);
        verify(categoryMapper).toUpdateCategoryResponse(updatedCategory);
    }

    @Test
    void givenCategoryExistsWhenDeleteCategoryThenDeleteCategory() {

        final var savedCategory = new Category(TEST_CATEGORY_UUID, TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);

        when(categoryRepository.findById(TEST_CATEGORY_UUID))
                .thenReturn(Optional.of(savedCategory));

        categoryService.deleteCategory(TEST_CATEGORY_UUID);

        verify(categoryRepository).findById(TEST_CATEGORY_UUID);
        verify(categoryRepository).delete(savedCategory);
    }

    @Test
    void givenCategoryNotExistsWhenDeleteCategoryThenThrowNotFoundException() {
        when(categoryRepository.findById(TEST_CATEGORY_UUID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.deleteCategory(TEST_CATEGORY_UUID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(CATEGORY_NOT_FOUND_MESSAGE.formatted(TEST_CATEGORY_UUID));

        verify(categoryRepository).findById(TEST_CATEGORY_UUID);
        verify(categoryRepository, never()).delete(any());
    }

}
