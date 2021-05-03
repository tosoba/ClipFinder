package com.clipfinder.soundcloud.api.model

import com.google.gson.annotations.SerializedName

class SoundCloudMedia(
    @SerializedName("transcodings") val transcodings: List<SoundCloudTranscodingsItem>?
)
