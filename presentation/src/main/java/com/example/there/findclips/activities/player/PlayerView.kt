package com.example.there.findclips.activities.player

import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.there.findclips.model.entities.Video
import com.example.there.findclips.model.entities.VideoPlaylist
import com.example.there.findclips.view.lists.RelatedVideosList
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener

data class PlayerView(
        val state: PlayerViewState,
        val relatedVideosAdapter: RelatedVideosList.Adapter,
        val relatedVideosItemDecoration: RecyclerView.ItemDecoration,
        val onRelatedVideosScrollListener: EndlessRecyclerOnScrollListener,
        val onFavouriteBtnClickListener: View.OnClickListener
) {
    val fragmentTabs = arrayOf("Related", "Other")
}

data class PlayerViewState(
        val videos: ObservableArrayList<Video> = ObservableArrayList(),
        val favouriteVideoPlaylists: ObservableArrayList<VideoPlaylist> = ObservableArrayList()
)