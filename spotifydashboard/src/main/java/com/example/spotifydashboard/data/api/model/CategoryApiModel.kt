package com.example.spotifydashboard.data.api.model

import com.example.core.model.StringUrlModel

class CategoryApiModel(
        val id: String,
        val icons: List<StringUrlModel>,
        val name: String
)