package com.example.there.domain.repo.spotify

import com.example.core.model.Resource
import com.example.there.domain.entity.ListPage
import com.example.there.domain.entity.spotify.*
import io.reactivex.Observable
import io.reactivex.Single

interface ISpotifyRemoteDataStore {

    val currentUser: Single<Resource<UserEntity>>

    fun searchAll(query: String, offset: Int): Single<Resource<SearchAllEntity>>

    fun getPlaylistsForCategory(categoryId: String, offset: Int): Single<Resource<ListPage<PlaylistEntity>>>

    fun getPlaylistTracks(playlistId: String, userId: String, offset: Int): Single<Resource<ListPage<TrackEntity>>>

    fun getAlbum(albumId: String): Single<Resource<AlbumEntity>>

    fun getArtists(artistIds: List<String>): Single<Resource<List<ArtistEntity>>>

    fun getSimilarTracks(trackId: String): Observable<Resource<List<TrackEntity>>>

    fun getAlbumsFromArtist(artistId: String): Observable<Resource<List<AlbumEntity>>>

    fun getTopTracksFromArtist(artistId: String): Single<Resource<List<TrackEntity>>>

    fun getRelatedArtists(artistId: String): Single<Resource<List<ArtistEntity>>>

    fun getTracksFromAlbum(albumId: String, offset: Int): Single<Resource<ListPage<TrackEntity>>>

    fun getCurrentUsersPlaylists(offset: Int): Single<Resource<ListPage<PlaylistEntity>>>
    fun getCurrentUsersTopTracks(offset: Int): Single<Resource<ListPage<TrackEntity>>>

    fun getCurrentUsersTopArtists(offset: Int): Single<Resource<ListPage<ArtistEntity>>>
    fun getCurrentUsersSavedTracks(offset: Int): Single<Resource<ListPage<TrackEntity>>>
    fun getCurrentUsersSavedAlbums(offset: Int): Single<Resource<ListPage<AlbumEntity>>>

    fun getAudioFeatures(trackEntity: TrackEntity): Single<Resource<AudioFeaturesEntity>>
}
