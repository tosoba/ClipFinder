package com.example.there.data.repos.spotify

import com.example.there.data.api.spotify.SpotifyAccountsApi
import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.data.api.spotify.SpotifyChartsApi
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
}