package com.proxy.ecpcatalogservice.dto;

import java.util.UUID;

public record CreateCategoryResponse(UUID id, String name, String description) {

}
