package com.example.there.findclips.fragments.favourites.spotify

import android.databinding.ObservableArrayList
import com.example.there.findclips.model.entities.*
import com.example.there.findclips.view.lists.*

data class SpotifyFavouritesFragmentView(
        val state: SpotifyFavouritesFragmentViewState,
        val albumsAdapter: AlbumsList.Adapter,
        val artistsAdapter: ArtistsList.Adapter,
        val categoriesAdapter: CategoriesList.Adapter,
        val playlistsAdapter: PlaylistsList.Adapter,
        val tracksAdapter: TracksList.Adapter
)

data class SpotifyFavouritesFragmentViewState(
        val albums: ObservableArrayList<Album> = ObservableArrayList(),
        val artists: ObservableArrayList<Artist> = ObservableArrayList(),
        val categories: ObservableArrayList<Category> = ObservableArrayList(),
        val playlists: ObservableArrayList<Playlist> = ObservableArrayList(),
        val tracks: ObservableArrayList<Track> = ObservableArrayList()
) {
    fun clearAll() {
        albums.clear()
        artists.clear()
        categories.clear()
        playlists.clear()
        tracks.clear()
    }
}