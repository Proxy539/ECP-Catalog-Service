package com.proxy.ecpcatalogservice.dto;

import java.util.UUID;

public record GetCategoryResponse(UUID id, String name, String description) {

}
