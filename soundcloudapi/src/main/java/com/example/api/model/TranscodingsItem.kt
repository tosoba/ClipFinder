package com.example.api.model

class TranscodingsItem(
        val duration: Int,
        val snipped: Boolean,
        val format: Format,
        val preset: String,
        val url: String,
        val quality: String
)