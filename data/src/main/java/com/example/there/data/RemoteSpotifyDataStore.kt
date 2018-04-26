package com.example.there.data

import android.util.Base64
import com.example.there.data.api.SpotifyAccountsApi
import com.example.there.data.api.SpotifyApi
import com.example.there.data.api.SpotifyChartsApi
import com.example.there.data.entities.AccessTokenData
import com.example.there.data.entities.CategoryData
import com.example.there.data.entities.PlaylistData
import com.example.there.data.entities.TrackData
import com.example.there.data.mapper.ChartTrackIdMapper
import com.example.there.data.response.TracksResponse
import com.example.there.domain.common.Mapper
import com.example.there.domain.SpotifyDataStore
import com.example.there.domain.entities.*
import io.reactivex.Observable

class RemoteSpotifyDataStore(private val api: SpotifyApi,
                             private val accountsApi: SpotifyAccountsApi,
                             private val chartsApi: SpotifyChartsApi,
                             private val categoryMapper: Mapper<CategoryData, CategoryEntity>,
                             private val playlistMapper: Mapper<PlaylistData, PlaylistEntity>,
                             private val trackMapper: Mapper<TrackData, TrackEntity>,
                             private val chartTrackIdMapper: Mapper<String, String>,
                             private val accessTokenMapper: Mapper<AccessTokenData, AccessTokenEntity>) : SpotifyDataStore {

    override fun getAccessToken(clientId: String, clientSecret: String): Observable<AccessTokenEntity> =
            accountsApi.getAccessToken(authorization = getClientDataHeader(clientId, clientSecret)).map(accessTokenMapper::mapFrom)

    override fun getCategories(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>> =
            api.getCategories(authorization = getAccessTokenHeader(accessToken.token))
                    .map { it.result.categories }
                    .map { it.map(categoryMapper::mapFrom) }

    override fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>> =
            api.getFeaturedPlaylists(authorization = getAccessTokenHeader(accessToken.token))
                    .map { it.result.playlists }
                    .map { it.map(playlistMapper::mapFrom) }

    override fun getTrack(accessToken: AccessTokenEntity, id: String): Observable<TrackEntity> =
            api.getTrack(authorization = getAccessTokenHeader(accessToken.token), id = id)
                    .map(trackMapper::mapFrom)

    override fun getDailyViralTracks(accessToken: AccessTokenEntity): Observable<List<TopTrackEntity>> {
        val trackIdsChunked: Observable<MutableList<List<String>>> = chartsApi.getDailyViralTracks()
                .map { it.split('\n').drop(1) }
                .map { it.map(chartTrackIdMapper::mapFrom) }
                .buffer(50)

        return trackIdsChunked
                .map {
                    it.mapIndexed { chunkIndex: Int, ids: List<String> ->
                        api.getTracks(authorization = getAccessTokenHeader(accessToken.token), ids = ids.joinToString(",").dropLast(1))
                                .map {
                                    it.tracks.mapIndexed { index: Int, trackData: TrackData ->
                                        TopTrackEntity(chunkIndex * 50 + index + 1, trackMapper.mapFrom(trackData))
                                    }
                                }
                    }
                }.flatMapIterable { it }.switchMap { it }
    }

    companion object {
        fun getAccessTokenHeader(accessToken: String): String = "Bearer $accessToken"

        fun getClientDataHeader(clientId: String, clientSecret: String): String {
            val encoded = Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)
            return "Basic $encoded"
        }
    }
}