package com.example.there.domain.repo.spotify.datastore

import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.*
import io.reactivex.Observable
import io.reactivex.Single

interface ISpotifyRemoteDataStore {
    fun getAccessToken(clientId: String, clientSecret: String): Single<AccessTokenEntity>

    fun getCategories(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>>

    fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>>

    fun getDailyViralTracks(accessToken: AccessTokenEntity): Observable<List<TopTrackEntity>>

    fun searchAll(accessToken: AccessTokenEntity, query: String): Observable<SearchAllEntity>

    fun getPlaylistsForCategory(accessToken: AccessTokenEntity, categoryId: String, offset: Int): Single<EntityPage<PlaylistEntity>>

    fun getPlaylistTracks(accessToken: AccessTokenEntity, playlistId: String, userId: String, offset: Int): Single<EntityPage<TrackEntity>>

    fun getAlbum(accessToken: AccessTokenEntity, albumId: String): Single<AlbumEntity>

    fun getArtists(accessToken: AccessTokenEntity, artistIds: List<String>): Single<List<ArtistEntity>>

    fun getSimilarTracks(accessToken: AccessTokenEntity, trackId: String): Observable<List<TrackEntity>>

    fun getAlbumsFromArtist(accessToken: AccessTokenEntity, artistId: String): Observable<List<AlbumEntity>>

    fun getTopTracksFromArtist(accessToken: AccessTokenEntity, artistId: String): Single<List<TrackEntity>>

    fun getRelatedArtists(accessToken: AccessTokenEntity, artistId: String): Single<List<ArtistEntity>>

    fun getTracksFromAlbum(accessToken: AccessTokenEntity, albumId: String, offset: Int): Single<EntityPage<TrackEntity>>

    fun getNewReleases(accessToken: AccessTokenEntity, offset: Int): Single<EntityPage<AlbumEntity>>

    fun getCurrentUsersPlaylists(accessToken: AccessTokenEntity, offset: Int): Single<EntityPage<PlaylistEntity>>
}