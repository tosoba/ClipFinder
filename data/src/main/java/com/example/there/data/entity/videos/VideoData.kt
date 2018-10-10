package com.example.there.data.entity.videos

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName


class VideoData(
        val id: String,
        val snippet: VideoSnippet,
        val contentDetails: VideoContentDetails,
        val statistics: Statistics
)

@Entity(
        tableName = "videos",
        foreignKeys = [
            ForeignKey(
                    entity = VideoPlaylistData::class,
                    parentColumns = ["id"],
                    childColumns = ["playlist_id"],
                    onDelete = ForeignKey.SET_NULL
            ),
            ForeignKey(
                    entity = VideoSearchDbData::class,
                    parentColumns = ["search_query"],
                    childColumns = ["search_query"],
                    onDelete = ForeignKey.SET_NULL
            ),
            ForeignKey(
                    entity = RelatedVideoSearchDbData::class,
                    parentColumns = ["related_video_id"],
                    childColumns = ["related_video_id"],
                    onDelete = ForeignKey.SET_NULL
            )
        ],
        indices = [
            Index(value = ["playlist_id"]),
            Index(value = ["search_query"]),
            Index(value = ["related_video_id"])
        ]
)
data class VideoDbData(
        @PrimaryKey
        val id: String,

        @ColumnInfo(name = "channel_id")
        val channelId: String,

        val title: String,

        val description: String,

        @ColumnInfo(name = "published_at")
        val publishedAt: String,

        @ColumnInfo(name = "thumbnail_url")
        val thumbnailUrl: String,

        val duration: String,

        @ColumnInfo(name = "view_count")
        val viewCount: Long,

        @ColumnInfo(name = "playlist_id")
        var playlistId: Long? = null,

        @ColumnInfo(name = "channel_thumbnail_url")
        val channelThumbnailUrl: String,

        @ColumnInfo(name = "search_query")
        var query: String? = null,

        @ColumnInfo(name = "related_video_id")
        var relatedVideoId: String? = null
)

class VideoSnippet(
        val publishedAt: String,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: ThumbnailsData,
        val channelTitle: String,
        val tags: List<String>
)

class VideoContentDetails(val duration: String)

class Statistics(
        val viewCount: String?,
        val likeCount: String?,
        val dislikeCount: String?,
        val favoriteCount: String?,
        val commentCount: String?
)

class VideoSearchData(val id: VideoSearchId)

class VideoSearchId(@SerializedName("videoId") val id: String)