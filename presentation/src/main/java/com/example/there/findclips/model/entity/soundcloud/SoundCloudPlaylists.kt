package com.example.there.findclips.model.entity.soundcloud

import android.os.Parcelable
import com.example.there.domain.entity.soundcloud.SoundCloudPlaylistEntity
import com.example.there.domain.entity.soundcloud.SoundCloudSystemPlaylistEntity
import com.example.there.findclips.R
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.imageview.ImageViewSrc
import com.example.there.findclips.view.list.item.NamedImageListItem
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoundCloudPlaylist(
        val artworkUrl: String?,
        val createdAt: String,
        val duration: Int,
        val id: Int,
        val likesCount: Int,
        val publishedAt: String?,
        val title: String,
        val trackCount: Int,
        val userId: Int
) : NamedImageListItem, Parcelable {

    @IgnoredOnParcel
    override val name: String = title

    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc(artworkUrl, R.drawable.playlist_placeholder, R.drawable.playlist_placeholder)

    override val foregroundDrawableId: Int
        get() = R.drawable.sound_cloud_foreground_ripple

    companion object {
        val sortedListCallback = object : ObservableSortedList.Callback<SoundCloudPlaylist> {
            override fun compare(
                    o1: SoundCloudPlaylist,
                    o2: SoundCloudPlaylist
            ): Int = o1.title.toLowerCase().compareTo(o2.title.toLowerCase())

            override fun areItemsTheSame(
                    item1: SoundCloudPlaylist,
                    item2: SoundCloudPlaylist
            ): Boolean = item1.id == item2.id

            override fun areContentsTheSame(
                    oldItem: SoundCloudPlaylist,
                    newItem: SoundCloudPlaylist
            ): Boolean = newItem.id == oldItem.id
        }
    }
}

val SoundCloudPlaylistEntity.ui: SoundCloudPlaylist
    get() = SoundCloudPlaylist(
            artworkUrl = artworkUrl,
            createdAt = createdAt,
            duration = duration,
            id = id,
            likesCount = likesCount,
            publishedAt = publishedAt,
            title = title,
            trackCount = trackCount,
            userId = userId
    )

@Parcelize
data class SoundCloudSystemPlaylist(
        val artworkUrl: String?,
        val description: String,
        val id: String,
        val shortDescription: String,
        val title: String,
        val trackIds: List<Int>
) : NamedImageListItem, Parcelable {

    @IgnoredOnParcel
    override val name: String = title

    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc(artworkUrl, R.drawable.playlist_placeholder, R.drawable.playlist_placeholder)

    override val foregroundDrawableId: Int
        get() = R.drawable.sound_cloud_foreground_ripple

    companion object {
        val sortedListCallback = object : ObservableSortedList.Callback<SoundCloudSystemPlaylist> {
            override fun compare(
                    o1: SoundCloudSystemPlaylist,
                    o2: SoundCloudSystemPlaylist
            ): Int = o1.title.toLowerCase().compareTo(o2.title.toLowerCase())

            override fun areItemsTheSame(
                    item1: SoundCloudSystemPlaylist,
                    item2: SoundCloudSystemPlaylist
            ): Boolean = item1.id == item2.id

            override fun areContentsTheSame(
                    oldItem: SoundCloudSystemPlaylist,
                    newItem: SoundCloudSystemPlaylist
            ): Boolean = oldItem.id == newItem.id
        }
    }
}

val SoundCloudSystemPlaylistEntity.ui: SoundCloudSystemPlaylist
    get() = SoundCloudSystemPlaylist(
            artworkUrl = calculatedArtworkUrl,
            description = description,
            id = id,
            shortDescription = shortDescription,
            title = title,
            trackIds = tracks.map { it.id }
    )

