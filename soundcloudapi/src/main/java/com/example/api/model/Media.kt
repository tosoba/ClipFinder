package com.example.api.model

import com.google.gson.annotations.SerializedName

class Media(@SerializedName("transcodings") val transcodings: List<TranscodingsItem>?)
