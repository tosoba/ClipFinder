package com.example.there.domain.repos.spotify.datastores

import com.example.there.domain.entities.spotify.*
import com.example.there.domain.pages.CategoryPlaylistsPage
import com.example.there.domain.pages.TracksPage
import io.reactivex.Observable
import io.reactivex.Single

interface ISpotifyRemoteDataStore {
    fun getAccessToken(clientId: String, clientSecret: String): Observable<AccessTokenEntity>

    fun getCategories(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>>

    fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>>

    fun getTrack(accessToken: AccessTokenEntity, id: String): Observable<TrackEntity>

    fun getDailyViralTracks(accessToken: AccessTokenEntity): Observable<List<TopTrackEntity>>

    fun searchAll(accessToken: AccessTokenEntity, query: String): Observable<SearchAllEntity>

    fun getPlaylistsForCategory(accessToken: AccessTokenEntity, categoryId: String, offset: Int): Single<CategoryPlaylistsPage>

    fun getPlaylistTracks(accessToken: AccessTokenEntity, playlistId: String, userId: String, offset: Int): Single<TracksPage>

    fun getAlbum(accessToken: AccessTokenEntity, albumId: String): Observable<AlbumEntity>

    fun getArtists(accessToken: AccessTokenEntity, artistIds: List<String>): Observable<List<ArtistEntity>>

    fun getSimilarTracks(accessToken: AccessTokenEntity, trackId: String): Observable<List<TrackEntity>>

    fun getAlbumsFromArtist(accessToken: AccessTokenEntity, artistId: String): Observable<List<AlbumEntity>>

    fun getTopTracksFromArtist(accessToken: AccessTokenEntity, artistId: String): Observable<List<TrackEntity>>

    fun getRelatedArtists(accessToken: AccessTokenEntity, artistId: String): Observable<List<ArtistEntity>>

    fun getTracksFromAlbum(accessToken: AccessTokenEntity, albumId: String, offset: Int): Single<TracksPage>
}