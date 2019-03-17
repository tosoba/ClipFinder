package com.example.spotifyapi.model

import com.example.core.model.StringUrlModel

class CategoryApiModel(
        val id: String,
        val icons: List<StringUrlModel>,
        val name: String
)