package com.proxy.ecpcatalogservice.repository;

import com.proxy.ecpcatalogservice.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
