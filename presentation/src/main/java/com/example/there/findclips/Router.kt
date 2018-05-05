package com.example.there.findclips

import android.support.v4.app.FragmentActivity
import com.example.there.findclips.category.CategoryActivity
import com.example.there.findclips.entities.Category
import com.example.there.findclips.entities.Playlist
import com.example.there.findclips.entities.Track
import com.example.there.findclips.playlist.PlaylistActivity
import com.example.there.findclips.trackvideos.TrackVideosActivity

object Router {

    fun goToTrackVideosActivity(activity: FragmentActivity?, track: Track) {
        activity?.let { TrackVideosActivity.start(it, track) }
    }

    fun goToCategoryActivity(activity: FragmentActivity?, category: Category) {
        activity?.let { CategoryActivity.start(it, category) }
    }

    fun goToPlaylistActivity(activity: FragmentActivity?, playlist: Playlist) {
        activity?.let { PlaylistActivity.start(it, playlist) }
    }
}