package com.example.there.findclips.entities

import android.databinding.ObservableField
import com.example.there.domain.util.Duration
import java.math.BigInteger

data class Video(
        val id: String,
        val channelId: String,
        val title: String,
        val description: String,
        val publishedAt: String,
        val thumbnailUrl: String,
        var duration: Duration,
        var viewCount: BigInteger,
        var channelThumbnailUrl: ObservableField<String>
)