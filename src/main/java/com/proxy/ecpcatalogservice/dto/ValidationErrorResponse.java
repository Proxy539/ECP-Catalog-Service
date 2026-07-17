package com.proxy.ecpcatalogservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ValidationErrorResponse(LocalDateTime timestamp, Integer status, String error, Map<String, List<String>> errors) {
}
