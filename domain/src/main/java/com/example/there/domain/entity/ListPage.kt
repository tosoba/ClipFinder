package com.example.there.domain.entity

class ListPage<T>(
        val items: List<T>,
        val offset: Int,
        val totalItems: Int
)