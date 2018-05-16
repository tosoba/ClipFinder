package com.example.there.data.repos.spotify

import com.example.there.data.apis.spotify.SpotifyAccountsApi
import com.example.there.data.apis.spotify.SpotifyApi
import com.example.there.data.apis.spotify.SpotifyChartsApi
import com.example.there.data.repos.spotify.stores.RemoteSpotifyDataStore
import com.example.there.domain.entities.spotify.*
import com.example.there.domain.repos.spotify.SpotifyDataStore
import com.example.there.domain.repos.spotify.SpotifyRepository
import io.reactivex.Observable

class SpotifyRepositoryImpl(api: SpotifyApi,
                            accountsApi: SpotifyAccountsApi,
                            chartsApi: SpotifyChartsApi) : SpotifyRepository {

    private val remoteSpotifyDataStore: SpotifyDataStore = RemoteSpotifyDataStore(api, accountsApi, chartsApi)

    override fun getAccessToken(clientId: String, clientSecret: String): Observable<AccessTokenEntity> =
            remoteSpotifyDataStore.getAccessToken(clientId, clientSecret)

    override fun getCategories(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>> =
            remoteSpotifyDataStore.getCategories(accessToken)

    override fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>> =
            remoteSpotifyDataStore.getFeaturedPlaylists(accessToken)

    override fun getTrack(accessToken: AccessTokenEntity, id: String): Observable<TrackEntity> =
            remoteSpotifyDataStore.getTrack(accessToken, id)

    override fun getDailyViralTracks(accessToken: AccessTokenEntity): Observable<List<TopTrackEntity>> =
            remoteSpotifyDataStore.getDailyViralTracks(accessToken)

    override fun searchAll(accessToken: AccessTokenEntity, query: String): Observable<SearchAllEntity> =
            remoteSpotifyDataStore.searchAll(accessToken, query)

    override fun getPlaylistsForCategory(accessToken: AccessTokenEntity, categoryId: String): Observable<List<PlaylistEntity>> =
            remoteSpotifyDataStore.getPlaylistsForCategory(accessToken, categoryId)

    override fun getPlaylistTracks(accessToken: AccessTokenEntity, playlistId: String, userId: String): Observable<List<TrackEntity>> =
            remoteSpotifyDataStore.getPlaylistTracks(accessToken, playlistId, userId)

    override fun getAlbum(accessToken: AccessTokenEntity, albumId: String): Observable<AlbumEntity> =
            remoteSpotifyDataStore.getAlbum(accessToken, albumId)

    override fun getArtists(accessToken: AccessTokenEntity, artistIds: List<String>): Observable<List<ArtistEntity>> =
            remoteSpotifyDataStore.getArtists(accessToken, artistIds)

    override fun getSimilarTracks(accessToken: AccessTokenEntity, trackId: String): Observable<List<TrackEntity>> =
            remoteSpotifyDataStore.getSimilarTracks(accessToken, trackId)

    override fun getAlbumsFromArtist(accessToken: AccessTokenEntity, artistId: String): Observable<List<AlbumEntity>> =
            remoteSpotifyDataStore.getAlbumsFromArtist(accessToken, artistId)

    override fun getTopTracksFromArtist(accessToken: AccessTokenEntity, artistId: String): Observable<List<TrackEntity>> =
            remoteSpotifyDataStore.getTopTracksFromArtist(accessToken, artistId)

    override fun getRelatedArtists(accessToken: AccessTokenEntity, artistId: String): Observable<List<ArtistEntity>> =
            remoteSpotifyDataStore.getRelatedArtists(accessToken, artistId)
}