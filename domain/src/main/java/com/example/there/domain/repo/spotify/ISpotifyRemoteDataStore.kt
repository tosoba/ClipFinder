package com.example.there.domain.repo.spotify

import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.*
import io.reactivex.Observable
import io.reactivex.Single

interface ISpotifyRemoteDataStore {

    val categories: Observable<List<CategoryEntity>>

    val featuredPlaylists: Observable<List<PlaylistEntity>>

    val dailyViralTracks: Observable<List<TopTrackEntity>>

    val currentUser: Single<UserEntity>

    fun searchAll(query: String, offset: Int): Single<SearchAllEntity>

    fun getPlaylistsForCategory(categoryId: String, offset: Int): Single<EntityPage<PlaylistEntity>>

    fun getPlaylistTracks(playlistId: String, userId: String, offset: Int): Single<EntityPage<TrackEntity>>

    fun getAlbum(albumId: String): Single<AlbumEntity>

    fun getArtists(artistIds: List<String>): Single<List<ArtistEntity>>

    fun getSimilarTracks(trackId: String): Observable<List<TrackEntity>>

    fun getAlbumsFromArtist(artistId: String): Observable<List<AlbumEntity>>

    fun getTopTracksFromArtist(artistId: String): Single<List<TrackEntity>>

    fun getRelatedArtists(artistId: String): Single<List<ArtistEntity>>

    fun getTracksFromAlbum(albumId: String): Observable<EntityPage<TrackEntity>>

    fun getNewReleases(offset: Int): Single<EntityPage<AlbumEntity>>

    fun getCurrentUsersPlaylists(offset: Int): Single<EntityPage<PlaylistEntity>>
    fun getCurrentUsersTopTracks(offset: Int): Single<EntityPage<TrackEntity>>

    fun getCurrentUsersTopArtists(offset: Int): Single<EntityPage<ArtistEntity>>
    fun getCurrentUsersSavedTracks(offset: Int): Single<EntityPage<TrackEntity>>
    fun getCurrentUsersSavedAlbums(offset: Int): Single<EntityPage<AlbumEntity>>

    fun getAudioFeatures(trackEntity: TrackEntity): Single<AudioFeaturesEntity>
}