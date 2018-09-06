package com.example.there.domain.repos.spotify

import com.example.there.domain.entities.spotify.*
import com.example.there.domain.pages.CategoryPlaylistsPage
import com.example.there.domain.pages.TracksPage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface ISpotifyRepository {
    fun getAccessToken(clientId: String, clientSecret: String): Observable<AccessTokenEntity>

    fun getCategories(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>>
    fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>>
    fun getDailyViralTracks(accessToken: AccessTokenEntity): Observable<List<TopTrackEntity>>

    fun searchAll(accessToken: AccessTokenEntity, query: String): Observable<SearchAllEntity>

    fun getPlaylistsForCategory(accessToken: AccessTokenEntity, categoryId: String, offset: Int): Single<CategoryPlaylistsPage>
    fun getPlaylistTracks(accessToken: AccessTokenEntity, playlistId: String, userId: String, offset: Int): Single<TracksPage>
    fun getTracksFromAlbum(accessToken: AccessTokenEntity, albumId: String, offset: Int): Single<TracksPage>
    fun getAlbum(accessToken: AccessTokenEntity, albumId: String): Single<AlbumEntity>
    fun getSimilarTracks(accessToken: AccessTokenEntity, trackId: String): Observable<List<TrackEntity>>
    fun getArtists(accessToken: AccessTokenEntity, artistIds: List<String>): Single<List<ArtistEntity>>
    fun getTopTracksFromArtist(accessToken: AccessTokenEntity, artistId: String): Single<List<TrackEntity>>
    fun getAlbumsFromArtist(accessToken: AccessTokenEntity, artistId: String): Observable<List<AlbumEntity>>
    fun getRelatedArtists(accessToken: AccessTokenEntity, artistId: String): Single<List<ArtistEntity>>

    fun getFavouriteAlbums(): Single<List<AlbumEntity>>
    fun getFavouriteArtists(): Single<List<ArtistEntity>>
    fun getFavouriteCategories(): Single<List<CategoryEntity>>
    fun getFavouritePlaylists(): Single<List<PlaylistEntity>>
    fun getFavouriteTracks(): Single<List<TrackEntity>>
    fun insertAlbum(albumEntity: AlbumEntity): Completable
    fun insertArtist(artistEntity: ArtistEntity): Completable
    fun insertCategory(categoryEntity: CategoryEntity): Completable
    fun insertPlaylist(playlistEntity: PlaylistEntity): Completable
    fun insertTrack(trackEntity: TrackEntity): Completable
}