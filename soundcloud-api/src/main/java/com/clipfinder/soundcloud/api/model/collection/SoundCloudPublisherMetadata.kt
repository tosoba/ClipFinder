package com.clipfinder.soundcloud.api.model.collection

import com.google.gson.annotations.SerializedName

data class SoundCloudPublisherMetadata(
    @SerializedName("album_title") val albumTitle: String,
    val artist: String,
    @SerializedName("contains_music") val containsMusic: Boolean,
    val explicit: Boolean,
    val id: Int,
    val isrc: String,
    val iswc: String,
    @SerializedName("p_line") val pLine: String,
    @SerializedName("p_line_for_display") val pLineForDisplay: String,
    val publisher: String,
    @SerializedName("release_title") val releaseTitle: String,
    @SerializedName("upc_or_ean") val upcOrEan: String,
    val urn: String,
    @SerializedName("writer_composer") val writerComposer: String
)
