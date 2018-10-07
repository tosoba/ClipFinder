package com.example.there.domain.repo.spotify

import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface ISpotifyRepository {
    fun getAccessToken(clientId: String, clientSecret: String): Single<AccessTokenEntity>

    fun getCategories(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>>
    fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>>
    fun getDailyViralTracks(accessToken: AccessTokenEntity): Observable<List<TopTrackEntity>>

    fun searchAll(accessToken: AccessTokenEntity, query: String, offset: Int): Single<SearchAllEntity>

    fun getPlaylistsForCategory(accessToken: AccessTokenEntity, categoryId: String, offset: Int): Single<EntityPage<PlaylistEntity>>
    fun getPlaylistTracks(accessToken: AccessTokenEntity, playlistId: String, userId: String, offset: Int): Single<EntityPage<TrackEntity>>
    fun getTracksFromAlbum(accessToken: AccessTokenEntity, albumId: String): Observable<EntityPage<TrackEntity>>
    fun getAlbum(accessToken: AccessTokenEntity, albumId: String): Single<AlbumEntity>
    fun getSimilarTracks(accessToken: AccessTokenEntity, trackId: String): Observable<List<TrackEntity>>
    fun getArtists(accessToken: AccessTokenEntity, artistIds: List<String>): Single<List<ArtistEntity>>
    fun getTopTracksFromArtist(accessToken: AccessTokenEntity, artistId: String): Single<List<TrackEntity>>
    fun getAlbumsFromArtist(accessToken: AccessTokenEntity, artistId: String): Observable<List<AlbumEntity>>
    fun getRelatedArtists(accessToken: AccessTokenEntity, artistId: String): Single<List<ArtistEntity>>
    fun getNewReleases(accessToken: AccessTokenEntity, offset: Int): Single<EntityPage<AlbumEntity>>

    fun getCurrentUsersPlaylists(accessToken: AccessTokenEntity, offset: Int): Single<EntityPage<PlaylistEntity>>
    fun getCurrentUsersTopTracks(accessToken: AccessTokenEntity, offset: Int): Single<EntityPage<TrackEntity>>
    fun getCurrentUsersTopArtists(accessToken: AccessTokenEntity, offset: Int): Single<EntityPage<ArtistEntity>>

    fun getCurrentUsersSavedTracks(accessToken: AccessTokenEntity, offset: Int): Single<EntityPage<TrackEntity>>
    fun getCurrentUsersSavedAlbums(accessToken: AccessTokenEntity, offset: Int): Single<EntityPage<AlbumEntity>>

    fun getFavouriteAlbums(): Flowable<List<AlbumEntity>>
    fun getFavouriteArtists(): Flowable<List<ArtistEntity>>
    fun getFavouriteCategories(): Flowable<List<CategoryEntity>>
    fun getFavouritePlaylists(): Flowable<List<PlaylistEntity>>
    fun getFavouriteTracks(): Flowable<List<TrackEntity>>
    fun insertAlbum(albumEntity: AlbumEntity): Completable
    fun insertArtist(artistEntity: ArtistEntity): Completable
    fun insertCategory(categoryEntity: CategoryEntity): Completable
    fun insertPlaylist(playlistEntity: PlaylistEntity): Completable
    fun insertTrack(trackEntity: TrackEntity): Completable

    fun getCurrentUser(accessToken: AccessTokenEntity): Single<UserEntity>

    fun isAlbumSaved(albumEntity: AlbumEntity): Single<Boolean>
    fun isArtistSaved(artistEntity: ArtistEntity): Single<Boolean>
    fun isCategorySaved(categoryEntity: CategoryEntity): Single<Boolean>
    fun isPlaylistSaved(playlistEntity: PlaylistEntity): Single<Boolean>
    fun isTrackSaved(trackEntity: TrackEntity): Single<Boolean>

    fun deleteAlbum(albumEntity: AlbumEntity): Completable
    fun deleteArtist(artistEntity: ArtistEntity): Completable
    fun deleteCategory(categoryEntity: CategoryEntity): Completable
    fun deletePlaylist(playlistEntity: PlaylistEntity): Completable
    fun deleteTrack(trackEntity: TrackEntity): Completable

    fun getAudioFeatures(accessToken: AccessTokenEntity, trackEntity: TrackEntity): Single<AudioFeaturesEntity>
}