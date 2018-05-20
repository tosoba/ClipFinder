package com.example.there.findclips.fragments.favourites.spotify

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.Router
import com.example.there.findclips.databinding.FragmentSpotifyFavouritesBinding
import com.example.there.findclips.model.entities.*
import com.example.there.findclips.view.lists.*


class SpotifyFavouritesFragment : Fragment() {

    val state: SpotifyFavouritesFragmentViewState = SpotifyFavouritesFragmentViewState()

    fun updateState(albums: List<Album>, artists: List<Artist>, categories: List<Category>, playlists: List<Playlist>, tracks: List<Track>) {
        state.clearAll()
        state.albums.addAll(albums)
        state.artists.addAll(artists)
        state.categories.addAll(categories)
        state.playlists.addAll(playlists)
        state.tracks.addAll(tracks)
    }

    private val onAlbumClickListener = object : OnAlbumClickListener {
        override fun onClick(item: Album) = Router.goToAlbumAcitivity(activity, album = item)
    }

    private val onArtistClickListener = object : OnArtistClickListener {
        override fun onClick(item: Artist) = Router.goToArtistActivity(activity, artist = item)
    }

    private val onCategoryClickListener = object : OnCategoryClickListener {
        override fun onClick(item: Category) = Router.goToCategoryActivity(activity, category = item)
    }

    private val onPlaylistClickListener = object : OnPlaylistClickListener {
        override fun onClick(item: Playlist) = Router.goToPlaylistActivity(activity, playlist = item)
    }

    private val onTrackClickListener = object : OnTrackClickListener {
        override fun onClick(item: Track) = Router.goToTrackVideosActivity(activity, track = item)
    }

    private val view: SpotifyFavouritesFragmentView by lazy {
        SpotifyFavouritesFragmentView(
                state = state,
                albumsAdapter = AlbumsList.Adapter(state.albums, R.layout.album_item, onAlbumClickListener),
                artistsAdapter = ArtistsList.Adapter(state.artists, R.layout.artist_item, onArtistClickListener),
                categoriesAdapter = CategoriesList.Adapter(state.categories, R.layout.category_item, onCategoryClickListener),
                playlistsAdapter = PlaylistsList.Adapter(state.playlists, R.layout.playlist_item, onPlaylistClickListener),
                tracksAdapter = TracksList.Adapter(state.tracks, R.layout.track_item, onTrackClickListener)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSpotifyFavouritesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_favourites, container, false)
        return binding.apply {
            this.view = this@SpotifyFavouritesFragment.view
            favouritesAlbumsRecyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
            favouritesArtistsRecyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
            favouritesCategoriesRecyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
            favouritesPlaylistsRecyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
            favouritesTracksRecyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        }.root
    }
}
