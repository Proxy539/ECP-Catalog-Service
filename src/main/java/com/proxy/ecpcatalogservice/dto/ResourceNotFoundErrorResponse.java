package com.proxy.ecpcatalogservice.dto;

import java.time.LocalDateTime;

public record ResourceNotFoundErrorResponse (LocalDateTime timestamp, Integer status, String error) {}
