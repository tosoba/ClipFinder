package com.clipfinder.soundcloud.api.model

import com.google.gson.annotations.SerializedName

class SoundCloudFormat(val protocol: String?, @SerializedName("mime_type") val mimeType: String?)
