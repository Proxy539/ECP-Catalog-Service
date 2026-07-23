package com.proxy.ecpcatalogservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public record Category (
        @Id
        @GeneratedValue
        String id, 

        @Column(nullable = false, length = 100)
        String name,

        @Column(length = 500)
        String description) {

}
