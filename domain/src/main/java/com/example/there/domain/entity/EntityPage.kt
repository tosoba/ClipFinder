package com.example.there.domain.entity

class EntityPage<T>(
        val items: List<T>,
        val offset: Int,
        val totalItems: Int
)