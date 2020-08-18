package com.clipfinder.soundcloud.api.model

class SoundCloudTranscodingsItem(
    val duration: Int,
    val snipped: Boolean,
    val format: SoundCloudFormat,
    val preset: String,
    val url: String,
    val quality: String
)
