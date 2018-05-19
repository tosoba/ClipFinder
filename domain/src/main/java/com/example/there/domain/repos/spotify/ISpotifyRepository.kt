package com.example.there.domain.repos.spotify

import com.example.there.domain.entities.spotify.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface ISpotifyRepository {
    fun getAccessToken(clientId: String, clientSecret: String): Observable<AccessTokenEntity>
    fun getCategories(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>>
    fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>>
    fun getTrack(accessToken: AccessTokenEntity, id: String): Observable<TrackEntity>
    fun getDailyViralTracks(accessToken: AccessTokenEntity): Observable<List<TopTrackEntity>>
    fun searchAll(accessToken: AccessTokenEntity, query: String): Observable<SearchAllEntity>
    fun getPlaylistsForCategory(accessToken: AccessTokenEntity, categoryId: String): Observable<List<PlaylistEntity>>
    fun getPlaylistTracks(accessToken: AccessTokenEntity, playlistId: String, userId: String): Observable<List<TrackEntity>>
    fun getAlbum(accessToken: AccessTokenEntity, albumId: String): Observable<AlbumEntity>
    fun getArtists(accessToken: AccessTokenEntity, artistIds: List<String>): Observable<List<ArtistEntity>>
    fun getSimilarTracks(accessToken: AccessTokenEntity, trackId: String): Observable<List<TrackEntity>>
    fun getAlbumsFromArtist(accessToken: AccessTokenEntity, artistId: String): Observable<List<AlbumEntity>>
    fun getTopTracksFromArtist(accessToken: AccessTokenEntity, artistId: String): Observable<List<TrackEntity>>
    fun getRelatedArtists(accessToken: AccessTokenEntity, artistId: String): Observable<List<ArtistEntity>>
    fun getTracksFromAlbum(accessToken: AccessTokenEntity, albumId: String): Observable<List<TrackEntity>>

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