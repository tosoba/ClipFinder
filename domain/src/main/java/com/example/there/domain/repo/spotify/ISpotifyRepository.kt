package com.example.there.domain.repo.spotify

import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.*
import com.example.there.domain.repo.*
import io.reactivex.Observable
import io.reactivex.Single

interface ISpotifyRepository :
        IFavouriteTrackRepository<TrackEntity>,
        IFavouritePlaylistRepository<PlaylistEntity>,
        IFavouriteAlbumRepository<AlbumEntity>,
        IFavouriteArtistRepository<ArtistEntity>,
        IFavouriteCategoryRepository<CategoryEntity> {

    val categories: Observable<List<CategoryEntity>>
    val featuredPlaylists: Observable<List<PlaylistEntity>>
    val dailyViralTracks: Observable<List<TopTrackEntity>>

    fun searchAll(query: String, offset: Int): Single<SearchAllEntity>

    fun getPlaylistsForCategory(categoryId: String, offset: Int): Single<EntityPage<PlaylistEntity>>
    fun getPlaylistTracks(playlistId: String, userId: String, offset: Int): Single<EntityPage<TrackEntity>>
    fun getTracksFromAlbum(albumId: String): Observable<EntityPage<TrackEntity>>
    fun getAlbum(albumId: String): Single<AlbumEntity>
    fun getSimilarTracks(trackId: String): Observable<List<TrackEntity>>
    fun getArtists(artistIds: List<String>): Single<List<ArtistEntity>>
    fun getTopTracksFromArtist(artistId: String): Single<List<TrackEntity>>
    fun getAlbumsFromArtist(artistId: String): Observable<List<AlbumEntity>>
    fun getRelatedArtists(artistId: String): Single<List<ArtistEntity>>
    fun getNewReleases(offset: Int): Single<EntityPage<AlbumEntity>>

    val currentUser: Single<UserEntity>
    fun getCurrentUsersPlaylists(offset: Int): Single<EntityPage<PlaylistEntity>>
    fun getCurrentUsersTopTracks(offset: Int): Single<EntityPage<TrackEntity>>
    fun getCurrentUsersTopArtists(offset: Int): Single<EntityPage<ArtistEntity>>
    fun getCurrentUsersSavedTracks(offset: Int): Single<EntityPage<TrackEntity>>
    fun getCurrentUsersSavedAlbums(offset: Int): Single<EntityPage<AlbumEntity>>

    fun getAudioFeatures(trackEntity: TrackEntity): Single<AudioFeaturesEntity>
}