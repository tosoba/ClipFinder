package com.example.api.model

import com.google.gson.annotations.SerializedName

class Format(
    val protocol: String?,

    @SerializedName("mime_type")
    val mimeType: String?
)
