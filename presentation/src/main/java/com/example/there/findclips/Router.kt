package com.example.there.findclips

import android.app.Activity
import com.example.there.findclips.activities.artist.ArtistActivity
import com.example.there.findclips.activities.category.CategoryActivity
import com.example.there.findclips.model.entities.*
import com.example.there.findclips.activities.playlist.PlaylistActivity
import com.example.there.findclips.activities.trackvideos.TrackVideosActivity

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
}