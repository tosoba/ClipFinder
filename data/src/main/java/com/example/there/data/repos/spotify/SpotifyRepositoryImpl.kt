package com.example.there.data.repos.spotify

import com.example.there.data.api.spotify.SpotifyAccountsApi
import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.data.api.spotify.SpotifyChartsApi
import com.example.there.data.entities.spotify.AccessTokenData
import com.example.there.data.entities.spotify.CategoryData
import com.example.there.data.entities.spotify.PlaylistData
import com.example.there.data.entities.spotify.TrackData
import com.example.there.data.repos.spotify.stores.RemoteSpotifyDataStore
import com.example.there.domain.common.Mapper
import com.example.there.domain.repos.spotify.SpotifyDataStore
import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.entities.spotify.*
import io.reactivex.Observable

class SpotifyRepositoryImpl(api: SpotifyApi,
                            accountsApi: SpotifyAccountsApi,
                            chartsApi: SpotifyChartsApi,
                            categoryMapper: Mapper<CategoryData, CategoryEntity>,
                            playlistMapper: Mapper<PlaylistData, PlaylistEntity>,
                            trackMapper: Mapper<TrackData, TrackEntity>,
                            chartTrackIdMapper: Mapper<String, String>,
                            accessTokenMapper: Mapper<AccessTokenData, AccessTokenEntity>) : SpotifyRepository {

    private val remoteSpotifyDataStore: SpotifyDataStore =
            RemoteSpotifyDataStore(api, accountsApi, chartsApi, categoryMapper, playlistMapper, trackMapper, chartTrackIdMapper, accessTokenMapper)

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
}