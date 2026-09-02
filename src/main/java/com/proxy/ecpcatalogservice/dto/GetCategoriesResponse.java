package com.proxy.ecpcatalogservice.dto;

import java.util.List;

public record GetCategoriesResponse(List<GetCategoryResponse> categories) {
}
