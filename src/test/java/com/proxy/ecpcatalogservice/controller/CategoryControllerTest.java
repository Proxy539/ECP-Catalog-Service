package com.proxy.ecpcatalogservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proxy.ecpcatalogservice.dto.CreateCategoryRequest;
import com.proxy.ecpcatalogservice.dto.CreateCategoryResponse;
import com.proxy.ecpcatalogservice.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    private static final String CATEGORIES_API = "/api/v1/categories";
    private static final String TEST_CATEGORY_NAME = "test category name";
    private static final String TEST_CATEGORY_DESCRIPTION = "test category description";
    private static final String TEST_CATEGORY_ID = UUID.randomUUID().toString();
    private static final String BAD_REQUEST_ERROR = "400";
    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed";
    private static final String BLANK_NAME_VALIDATION_ERROR_MESSAGE = "Category name should not be blank";
    private static final String BLANK_CATEGORY_VALIDATION_ERROR_MESSAGE = "Category description should not be blank";
    private static final String LONG_CATEGORY_NAME_VALIDATION_ERROR_MESSAGE = "Category name should be max 100 characters long";
    private static final String LONG_CATEGORY_DESCRIPTION_VALIDATION_ERROR_MESSAGE = "Category description should be max 500 characters long";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenBlankCreateCategoryRequestFieldsWhenCreateCategoryRequestThenReturnBadRequestError() throws Exception {

        final var invalidCreateCategoryRequest = new CreateCategoryRequest(null, "");

        mockMvc.perform(post(CATEGORIES_API)
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
        final var invalidCreateCategoryRequest = new CreateCategoryRequest(TEST_CATEGORY_NAME.repeat(10), TEST_CATEGORY_DESCRIPTION.repeat(100));

        mockMvc.perform(post(CATEGORIES_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateCategoryRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST_ERROR))
                .andExpect(jsonPath("$.error").value(VALIDATION_FAILED_MESSAGE))
                .andExpect(jsonPath("$.errors.name[0]").value(LONG_CATEGORY_NAME_VALIDATION_ERROR_MESSAGE))
                .andExpect(jsonPath("$.errors.description[0]").value(LONG_CATEGORY_DESCRIPTION_VALIDATION_ERROR_MESSAGE));
    }

    @Test
    public void givenValidCreateCategoryRequestWhenCreateCategoryRequestThenReturnCreateCategoryResponse() throws Exception {
        final var validCreateCategoryRequest = new CreateCategoryRequest(TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);
        final var expectedCreateCategoryResponse = new CreateCategoryResponse(TEST_CATEGORY_ID, TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);

        when(categoryService.createCategory(validCreateCategoryRequest))
                .thenReturn(expectedCreateCategoryResponse);

        mockMvc.perform(post(CATEGORIES_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateCategoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TEST_CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(TEST_CATEGORY_NAME))
                .andExpect(jsonPath("$.description").value(TEST_CATEGORY_DESCRIPTION));

        verify(categoryService).createCategory(validCreateCategoryRequest);

    }

    @Test
    public void givenCreateCategoryRequestWithEmptyDescriptionWhenCreateCategoryRequestThenReturnCreateCategoryResponse() throws Exception {
        final var validCreateCategoryRequest = new CreateCategoryRequest(TEST_CATEGORY_NAME, null);

        final var expectedCreateCategoryResponse = new CreateCategoryResponse(TEST_CATEGORY_ID, TEST_CATEGORY_NAME, null);

        when(categoryService.createCategory(validCreateCategoryRequest))
                .thenReturn(expectedCreateCategoryResponse);

        mockMvc.perform(post(CATEGORIES_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validCreateCategoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TEST_CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(TEST_CATEGORY_NAME));

    }

}