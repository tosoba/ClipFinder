package com.example.there.data

import com.example.there.data.api.SpotifyAccountsApi
import com.example.there.data.api.SpotifyApi
import com.example.there.data.api.SpotifyChartsApi
import com.example.there.data.entities.AccessTokenData
import com.example.there.data.entities.CategoryData
import com.example.there.data.entities.PlaylistData
import com.example.there.data.entities.TrackData
import com.example.there.data.mapper.PlaylistMapper
import com.example.there.data.mapper.TrackMapper
import com.example.there.domain.common.Mapper
import com.example.there.domain.SpotifyDataStore
import com.example.there.domain.SpotifyRepository
import com.example.there.domain.entities.*
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