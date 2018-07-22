package com.example.there.data.repos.spotify

import com.example.there.domain.entities.spotify.*
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.repos.spotify.datastores.ISpotifyDbDataStore
import com.example.there.domain.repos.spotify.datastores.ISpotifyRemoteDataStore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class SpotifyRepository(private val remoteDataStore: ISpotifyRemoteDataStore,
                        private val dbDataStore: ISpotifyDbDataStore) : ISpotifyRepository {

    override fun getAccessToken(clientId: String, clientSecret: String): Observable<AccessTokenEntity> =
            remoteDataStore.getAccessToken(clientId, clientSecret)

    override fun getCategories(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>> =
            remoteDataStore.getCategories(accessToken)

    override fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>> =
            remoteDataStore.getFeaturedPlaylists(accessToken)

    override fun getTrack(accessToken: AccessTokenEntity, id: String): Observable<TrackEntity> =
            remoteDataStore.getTrack(accessToken, id)

    override fun getDailyViralTracks(accessToken: AccessTokenEntity): Observable<List<TopTrackEntity>> =
            remoteDataStore.getDailyViralTracks(accessToken)

    override fun searchAll(accessToken: AccessTokenEntity, query: String): Observable<SearchAllEntity> =
            remoteDataStore.searchAll(accessToken, query)

    override fun getPlaylistsForCategory(accessToken: AccessTokenEntity, categoryId: String): Observable<List<PlaylistEntity>> =
            remoteDataStore.getPlaylistsForCategory(accessToken, categoryId)

    override fun getPlaylistTracks(accessToken: AccessTokenEntity, playlistId: String, userId: String): Observable<List<TrackEntity>> =
            remoteDataStore.getPlaylistTracks(accessToken, playlistId, userId)

    override fun getAlbum(accessToken: AccessTokenEntity, albumId: String): Observable<AlbumEntity> =
            remoteDataStore.getAlbum(accessToken, albumId)

    override fun getArtists(accessToken: AccessTokenEntity, artistIds: List<String>): Observable<List<ArtistEntity>> =
            remoteDataStore.getArtists(accessToken, artistIds)

    override fun getSimilarTracks(accessToken: AccessTokenEntity, trackId: String): Observable<List<TrackEntity>> =
            remoteDataStore.getSimilarTracks(accessToken, trackId)

    override fun getAlbumsFromArtist(accessToken: AccessTokenEntity, artistId: String): Observable<List<AlbumEntity>> =
            remoteDataStore.getAlbumsFromArtist(accessToken, artistId)

    override fun getTopTracksFromArtist(accessToken: AccessTokenEntity, artistId: String): Observable<List<TrackEntity>> =
            remoteDataStore.getTopTracksFromArtist(accessToken, artistId)

    override fun getRelatedArtists(accessToken: AccessTokenEntity, artistId: String): Observable<List<ArtistEntity>> =
            remoteDataStore.getRelatedArtists(accessToken, artistId)

    override fun getTracksFromAlbum(accessToken: AccessTokenEntity, albumId: String): Observable<List<TrackEntity>> =
            remoteDataStore.getTracksFromAlbum(accessToken, albumId)

    override fun getFavouriteAlbums(): Single<List<AlbumEntity>> = dbDataStore.getFavouriteAlbums()

    override fun getFavouriteArtists(): Single<List<ArtistEntity>> = dbDataStore.getFavouriteArtists()

    override fun getFavouriteCategories(): Single<List<CategoryEntity>> = dbDataStore.getFavouriteCategories()

    override fun getFavouritePlaylists(): Single<List<PlaylistEntity>> = dbDataStore.getFavouritePlaylists()

    override fun getFavouriteTracks(): Single<List<TrackEntity>> = dbDataStore.getFavouriteTracks()

    override fun insertAlbum(albumEntity: AlbumEntity): Completable = dbDataStore.insertAlbum(albumEntity)

    override fun insertArtist(artistEntity: ArtistEntity): Completable = dbDataStore.insertArtist(artistEntity)

    override fun insertCategory(categoryEntity: CategoryEntity): Completable = dbDataStore.insertCategory(categoryEntity)

    override fun insertPlaylist(playlistEntity: PlaylistEntity): Completable = dbDataStore.insertPlaylist(playlistEntity)

    override fun insertTrack(trackEntity: TrackEntity): Completable = dbDataStore.insertTrack(trackEntity)
}