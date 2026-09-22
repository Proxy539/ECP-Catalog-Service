package com.proxy.ecpcatalogservice.dto;

import java.util.UUID;

public record UpdateCategoryResponse(UUID id, String name, String description) {
}
