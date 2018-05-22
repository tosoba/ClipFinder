package com.example.there.findclips

import android.app.Activity
import com.example.there.findclips.activities.album.AlbumActivity
import com.example.there.findclips.activities.artist.ArtistActivity
import com.example.there.findclips.activities.category.CategoryActivity
import com.example.there.findclips.activities.player.PlayerActivity
import com.example.there.findclips.activities.playlist.PlaylistActivity
import com.example.there.findclips.activities.trackvideos.TrackVideosActivity
import com.example.there.findclips.model.entities.*

object Router {
    fun goToTrackVideosActivity(activity: Activity?, track: Track) {
        activity?.let { TrackVideosActivity.start(it, track) }
    }

    fun goToCategoryActivity(activity: Activity?, category: Category) {
        activity?.let { CategoryActivity.start(it, category) }
    }

    fun goToPlaylistActivity(activity: Activity?, playlist: Playlist) {
        activity?.let { PlaylistActivity.start(it, playlist) }
    }

    fun goToArtistActivity(activity: Activity?, artist: Artist) {
        activity?.let { ArtistActivity.start(it, artist) }
    }

    fun goToAlbumAcitivity(activity: Activity?, album: Album) {
        activity?.let { AlbumActivity.start(it, album) }
    }

    fun goToPlayerActivity(activity: Activity?, video: Video, otherVideos: ArrayList<Video>) {
        activity?.let { PlayerActivity.start(it, video, otherVideos) }
    }
}