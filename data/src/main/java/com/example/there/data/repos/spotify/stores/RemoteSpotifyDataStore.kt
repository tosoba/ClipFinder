package com.example.there.data.repos.spotify.stores

import android.util.Base64
import com.example.there.data.api.spotify.SpotifyAccountsApi
import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.data.api.spotify.SpotifyChartsApi
import com.example.there.data.entities.spotify.TrackData
import com.example.there.data.mapper.spotify.*
import com.example.there.domain.entities.spotify.*
import com.example.there.domain.repos.spotify.SpotifyDataStore
import io.reactivex.Observable

class RemoteSpotifyDataStore(private val api: SpotifyApi,
                             private val accountsApi: SpotifyAccountsApi,
                             private val chartsApi: SpotifyChartsApi) : SpotifyDataStore {

    override fun getAccessToken(clientId: String, clientSecret: String): Observable<AccessTokenEntity> =
            accountsApi.getAccessToken(authorization = getClientDataHeader(clientId, clientSecret)).map(AccessTokenMapper::mapFrom)

    override fun getCategories(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>> =
            api.getCategories(authorization = getAccessTokenHeader(accessToken.token))
                    .map { it.result.categories }
                    .map { it.map(CategoryMapper::mapFrom) }

    override fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>> =
            api.getFeaturedPlaylists(authorization = getAccessTokenHeader(accessToken.token))
                    .map { it.result.playlists }
                    .map { it.map(PlaylistMapper::mapFrom) }

    override fun getTrack(accessToken: AccessTokenEntity, id: String): Observable<TrackEntity> =
            api.getTrack(authorization = getAccessTokenHeader(accessToken.token), id = id)
                    .map(TrackMapper::mapFrom)


    //TODO: buffer -> chunked
    override fun getDailyViralTracks(accessToken: AccessTokenEntity): Observable<List<TopTrackEntity>> {
        val trackIdsChunked: Observable<MutableList<List<String>>> = chartsApi.getDailyViralTracks()
                .map { it.split('\n').drop(1) }
                .map { it.map(ChartTrackIdMapper::mapFrom) }
                .buffer(50)

        return trackIdsChunked
                .map {
                    it.mapIndexed { chunkIndex: Int, ids: List<String> ->
                        api.getTracks(authorization = getAccessTokenHeader(accessToken.token), ids = ids.joinToString(",").dropLast(1))
                                .map {
                                    it.tracks.mapIndexed { index: Int, trackData: TrackData ->
                                        TopTrackEntity(chunkIndex * 50 + index + 1, TrackMapper.mapFrom(trackData))
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