package com.proxy.ecpcatalogservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proxy.ecpcatalogservice.dto.CreateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.CreateCategoryResponse;
import com.proxy.ecpcatalogservice.dto.GetCategoriesResponse;
import com.proxy.ecpcatalogservice.dto.GetCategoryResponse;
import com.proxy.ecpcatalogservice.exception.ResourceNotFoundException;
import com.proxy.ecpcatalogservice.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    private static final String GET_CATEGORIES_API = "/api/v1/categories";
    private static final String SAVE_CATEGORIES_API = "/api/v1/categories";
    private static final String GET_CATEGORY_BY_ID_API = "/api/v1/categories/{id}";
    private static final String TEST_CATEGORY_NAME = "test category name";
    private static final String TEST_CATEGORY_DESCRIPTION = "test category description";
    private static final UUID TEST_CATEGORY_UUID = UUID.randomUUID();
    private static final String BAD_REQUEST_ERROR = "400";
    private static final String NOT_FOUND_ERROR = "404";
    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed";
    private static final String BLANK_NAME_VALIDATION_ERROR_MESSAGE = "Category name should not be blank";
    private static final String LONG_CATEGORY_NAME_VALIDATION_ERROR_MESSAGE = "Category name should be max 100 characters long";
    private static final String LONG_CATEGORY_DESCRIPTION_VALIDATION_ERROR_MESSAGE = "Category description should be max 500 characters long";
    private final static String CATEGORY_NOT_FOUND_MESSAGE = "Category not found by id %s";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenBlankCreateCategoryRequestFieldsWhenCreateCategoryRequestThenReturnBadRequestError()
            throws Exception {

        final var invalidCreateCategoryRequest = new CreateCategoryRequest(null, "");

        mockMvc.perform(post(SAVE_CATEGORIES_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateCategoryRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST_ERROR))
                .andExpect(jsonPath("$.error").value(VALIDATION_FAILED_MESSAGE))
                .andExpect(jsonPath("$.errors.name[0]").value(BLANK_NAME_VALIDATION_ERROR_MESSAGE));
    }

    @Test
    public void givenLongCreateCategoryRequestFieldsWhenCreateCategoryRequestThenReturnBadRequest() throws Exception {
        final var invalidCreateCategoryRequest = new CreateCategoryRequest(TEST_CATEGORY_NAME.repeat(10),
                TEST_CATEGORY_DESCRIPTION.repeat(100));

        mockMvc.perform(post(SAVE_CATEGORIES_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateCategoryRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST_ERROR))
                .andExpect(jsonPath("$.error").value(VALIDATION_FAILED_MESSAGE))
                .andExpect(jsonPath("$.errors.name[0]").value(LONG_CATEGORY_NAME_VALIDATION_ERROR_MESSAGE))
                .andExpect(
                        jsonPath("$.errors.description[0]").value(LONG_CATEGORY_DESCRIPTION_VALIDATION_ERROR_MESSAGE));
    }

    @Test
    public void givenCategoryNotExistWhenGetCategoryThenReturnNotFound() throws Exception {
        final var resourceNotFoundException = new ResourceNotFoundException(
                CATEGORY_NOT_FOUND_MESSAGE.formatted(TEST_CATEGORY_UUID));
        when(categoryService.getCategory(TEST_CATEGORY_UUID))
                .thenThrow(resourceNotFoundException);

        mockMvc.perform(get(GET_CATEGORY_BY_ID_API, TEST_CATEGORY_UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(NOT_FOUND_ERROR))
                .andExpect(jsonPath("$.error").value(CATEGORY_NOT_FOUND_MESSAGE.formatted(TEST_CATEGORY_UUID)));

        verify(categoryService).getCategory(TEST_CATEGORY_UUID);
    }

    @Test
    public void givenCategoryExistsWhenGetCategoryThenReturnCategoryResponse() throws Exception {
        final var getCategoryResponse = new GetCategoryResponse(TEST_CATEGORY_UUID, TEST_CATEGORY_NAME,
                TEST_CATEGORY_DESCRIPTION);

        when(categoryService.getCategory(TEST_CATEGORY_UUID))
                .thenReturn(getCategoryResponse);

        mockMvc.perform(get(GET_CATEGORY_BY_ID_API, TEST_CATEGORY_UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_CATEGORY_UUID.toString()))
                .andExpect(jsonPath("$.name").value(TEST_CATEGORY_NAME))
                .andExpect(jsonPath("$.description").value(TEST_CATEGORY_DESCRIPTION));

        verify(categoryService).getCategory(TEST_CATEGORY_UUID);
    }

    @Test
    public void givenValidCreateCategoryRequestWhenCreateCategoryRequestThenReturnCreateCategoryResponse()
            throws Exception {
        final var validCreateCategoryRequest = new CreateCategoryRequest(TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);
        final var expectedCreateCategoryResponse = new CreateCategoryResponse(TEST_CATEGORY_UUID, TEST_CATEGORY_NAME,
                TEST_CATEGORY_DESCRIPTION);

        when(categoryService.createCategory(validCreateCategoryRequest))
                .thenReturn(expectedCreateCategoryResponse);

        mockMvc.perform(post(SAVE_CATEGORIES_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateCategoryRequest)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id").value(TEST_CATEGORY_UUID.toString()))
                .andExpect(jsonPath("$.name").value(TEST_CATEGORY_NAME))
                .andExpect(jsonPath("$.description").value(TEST_CATEGORY_DESCRIPTION));

        verify(categoryService).createCategory(validCreateCategoryRequest);

    }

    @Test
    public void givenCreateCategoryRequestWithEmptyDescriptionWhenCreateCategoryRequestThenReturnCreateCategoryResponse()
            throws Exception {
        final var validCreateCategoryRequest = new CreateCategoryRequest(TEST_CATEGORY_NAME, null);

        final var expectedCreateCategoryResponse = new CreateCategoryResponse(TEST_CATEGORY_UUID, TEST_CATEGORY_NAME,
                null);

        when(categoryService.createCategory(validCreateCategoryRequest))
                .thenReturn(expectedCreateCategoryResponse);

        mockMvc.perform(post(SAVE_CATEGORIES_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateCategoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TEST_CATEGORY_UUID.toString()))
                .andExpect(jsonPath("$.name").value(TEST_CATEGORY_NAME));

    }

    @Test
    public void givenCategoryExistsWhenGetCategoriesThenReturnGetCategoriesResponse() throws Exception {

        final var getCategoryResponse = new GetCategoryResponse(TEST_CATEGORY_UUID, TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);
        final var categories = List.of(getCategoryResponse);
        final var getCategoriesResponse = new GetCategoriesResponse(categories);

        when(categoryService.getCategories()).thenReturn(getCategoriesResponse);

        mockMvc.perform(get(GET_CATEGORIES_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories[0].id").value(TEST_CATEGORY_UUID.toString()))
                .andExpect(jsonPath("$.categories[0].name").value(TEST_CATEGORY_NAME))
                .andExpect(jsonPath("$.categories[0].description").value(TEST_CATEGORY_DESCRIPTION));

    }

    @Test
    public void givenNoCategoriesExistWhenGetCategoriesThenReturnEmptyList() throws Exception {

        final var getCategoriesResponse = new GetCategoriesResponse(List.of());

        when(categoryService.getCategories()).thenReturn(getCategoriesResponse);

        mockMvc.perform(get(GET_CATEGORIES_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories").isArray())
                .andExpect(jsonPath("$.categories").isEmpty());

    }

}
