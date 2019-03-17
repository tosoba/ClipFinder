package com.example.there.findclips.spotify.list

import android.support.v4.app.Fragment
import com.example.there.findclips.R
import com.example.there.findclips.spotify.spotifyitem.playlist.PlaylistFragment


class SpotifyPlaylistsFragment : BaseListFragment<Playlist>() {

    override val defaultHeaderText: String = "Playlists"

    override val viewState: ViewState<Playlist> = ViewState(ObservableSortedList<Playlist>(Playlist::class.java, IdentifiableObservableListItem.unsortedCallback()))

    override val listItemView: ListItemView<Playlist>
        get() = object : ListItemView<Playlist>(viewState.items) {
            override val itemViewBinder: ItemBinder<Playlist>
                get() = ItemBinderBase(BR.playlist, R.layout.grid_playlist_item)
        }

    override fun fragmentToShowOnItemClick(item: Playlist): Fragment = PlaylistFragment.newInstance(item)
}
