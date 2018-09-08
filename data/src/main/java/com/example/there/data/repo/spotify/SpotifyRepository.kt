package com.example.there.data.repo.spotify

import com.example.there.domain.entity.spotify.*
import com.example.there.domain.entitypage.CategoryPlaylistsPage
import com.example.there.domain.entitypage.TracksPage
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.repo.spotify.datastore.ISpotifyDbDataStore
import com.example.there.domain.repo.spotify.datastore.ISpotifyRemoteDataStore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class SpotifyRepository @Inject constructor(
        private val remoteDataStore: ISpotifyRemoteDataStore,
        private val dbDataStore: ISpotifyDbDataStore
) : ISpotifyRepository {

    override fun getAccessToken(
            clientId: String, clientSecret: String
    ): Single<AccessTokenEntity> = remoteDataStore.getAccessToken(clientId, clientSecret)

    override fun getCategories(
            accessToken: AccessTokenEntity
    ): Observable<List<CategoryEntity>> = remoteDataStore.getCategories(accessToken)

    override fun getFeaturedPlaylists(
            accessToken: AccessTokenEntity
    ): Observable<List<PlaylistEntity>> = remoteDataStore.getFeaturedPlaylists(accessToken)

    override fun getDailyViralTracks(
            accessToken: AccessTokenEntity
    ): Observable<List<TopTrackEntity>> = remoteDataStore.getDailyViralTracks(accessToken)

    override fun searchAll(accessToken: AccessTokenEntity, query: String): Observable<SearchAllEntity> =
            remoteDataStore.searchAll(accessToken, query)

    override fun getPlaylistsForCategory(
            accessToken: AccessTokenEntity,
            categoryId: String,
            offset: Int
    ): Single<CategoryPlaylistsPage> = remoteDataStore.getPlaylistsForCategory(accessToken, categoryId, offset)

    override fun getPlaylistTracks(
            accessToken: AccessTokenEntity,
            playlistId: String,
            userId: String,
            offset: Int
    ): Single<TracksPage> = remoteDataStore.getPlaylistTracks(accessToken, playlistId, userId, offset)

    override fun getAlbum(
            accessToken: AccessTokenEntity,
            albumId: String
    ): Single<AlbumEntity> = remoteDataStore.getAlbum(accessToken, albumId)

    override fun getArtists(
            accessToken: AccessTokenEntity,
            artistIds: List<String>
    ): Single<List<ArtistEntity>> = remoteDataStore.getArtists(accessToken, artistIds)

    override fun getSimilarTracks(
            accessToken: AccessTokenEntity,
            trackId: String
    ): Observable<List<TrackEntity>> = remoteDataStore.getSimilarTracks(accessToken, trackId)

    override fun getAlbumsFromArtist(accessToken: AccessTokenEntity, artistId: String): Observable<List<AlbumEntity>> =
            remoteDataStore.getAlbumsFromArtist(accessToken, artistId)

    override fun getTopTracksFromArtist(
            accessToken: AccessTokenEntity,
            artistId: String
    ): Single<List<TrackEntity>> = remoteDataStore.getTopTracksFromArtist(accessToken, artistId)

    override fun getRelatedArtists(
            accessToken: AccessTokenEntity,
            artistId: String
    ): Single<List<ArtistEntity>> = remoteDataStore.getRelatedArtists(accessToken, artistId)

    override fun getTracksFromAlbum(
            accessToken: AccessTokenEntity,
            albumId: String,
            offset: Int
    ): Single<TracksPage> = remoteDataStore.getTracksFromAlbum(accessToken, albumId, offset)

    override fun getFavouriteAlbums(): Flowable<List<AlbumEntity>> = dbDataStore.getFavouriteAlbums()

    override fun getFavouriteArtists(): Flowable<List<ArtistEntity>> = dbDataStore.getFavouriteArtists()

    override fun getFavouriteCategories(): Flowable<List<CategoryEntity>> = dbDataStore.getFavouriteCategories()

    override fun getFavouritePlaylists(): Flowable<List<PlaylistEntity>> = dbDataStore.getFavouritePlaylists()

    override fun getFavouriteTracks(): Flowable<List<TrackEntity>> = dbDataStore.getFavouriteTracks()

    override fun insertAlbum(albumEntity: AlbumEntity): Completable = dbDataStore.insertAlbum(albumEntity)

    override fun insertArtist(artistEntity: ArtistEntity): Completable = dbDataStore.insertArtist(artistEntity)

    override fun insertCategory(categoryEntity: CategoryEntity): Completable = dbDataStore.insertCategory(categoryEntity)

    override fun insertPlaylist(playlistEntity: PlaylistEntity): Completable = dbDataStore.insertPlaylist(playlistEntity)

    override fun insertTrack(trackEntity: TrackEntity): Completable = dbDataStore.insertTrack(trackEntity)
}