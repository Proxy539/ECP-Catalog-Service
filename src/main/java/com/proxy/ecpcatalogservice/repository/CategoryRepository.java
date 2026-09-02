package com.proxy.ecpcatalogservice.repository;

import com.proxy.ecpcatalogservice.model.Category;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
