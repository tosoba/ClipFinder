package com.example.there.findclips

import android.app.Activity
import com.example.there.findclips.category.CategoryActivity
import com.example.there.findclips.entities.Category
import com.example.there.findclips.entities.Playlist
import com.example.there.findclips.entities.Track
import com.example.there.findclips.entities.Video
import com.example.there.findclips.playlist.PlaylistActivity
import com.example.there.findclips.trackvideos.TrackVideosActivity

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
}