package com.example.db.model.videos

import android.arch.persistence.room.*

@Entity(
        tableName = "videos",
        foreignKeys = [
            ForeignKey(
                    entity = VideoPlaylistDbModel::class,
                    parentColumns = ["id"],
                    childColumns = ["playlist_id"],
                    onDelete = ForeignKey.SET_NULL
            ),
            ForeignKey(
                    entity = VideoSearchDbModel::class,
                    parentColumns = ["search_query"],
                    childColumns = ["search_query"],
                    onDelete = ForeignKey.SET_NULL
            ),
            ForeignKey(
                    entity = RelatedVideoSearchDbModel::class,
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
data class VideoDbModel(
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