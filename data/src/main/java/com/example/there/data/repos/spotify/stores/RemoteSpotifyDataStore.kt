package com.example.there.data.repos.spotify.stores

import android.util.Base64
import com.example.there.data.apis.spotify.SpotifyAccountsApi
import com.example.there.data.apis.spotify.SpotifyApi
import com.example.there.data.apis.spotify.SpotifyChartsApi
import com.example.there.data.entities.spotify.TrackData
import com.example.there.data.mappers.spotify.*
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

    override fun getDailyViralTracks(accessToken: AccessTokenEntity): Observable<List<TopTrackEntity>> = chartsApi.getDailyViralTracks()
            .map { it.split('\n').drop(1) }
            .map { it.map(ChartTrackIdMapper::mapFrom) }
            .buffer(50)
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

    override fun searchAll(accessToken: AccessTokenEntity, query: String): Observable<SearchAllEntity> =
            api.searchAll(authorization = getAccessTokenHeader(accessToken.token), query = query)
                    .map {
                        SearchAllEntity(
                                albums = it.albumsResult?.albums?.map(AlbumMapper::mapFrom)
                                        ?: emptyList(),
                                artists = it.artistsResult?.artists?.map(ArtistMapper::mapFrom)
                                        ?: emptyList(),
                                playlists = it.playlistsResult?.playlists?.map(PlaylistMapper::mapFrom)
                                        ?: emptyList(),
                                tracks = it.tracksResult?.tracks?.map(TrackMapper::mapFrom)
                                        ?: emptyList(),
                                albumsNextPageUrl = it.albumsResult?.nextPageUrl,
                                artistsNextPageUrl = it.artistsResult?.nextPageUrl,
                                playlistsNextPageUrl = it.playlistsResult?.nextPageUrl,
                                tracksNextPageUrl = it.tracksResult?.nextPageUrl
                        )
                    }

    override fun getPlaylistsForCategory(accessToken: AccessTokenEntity, categoryId: String): Observable<List<PlaylistEntity>> =
            api.getPlaylistsForCategory(authorization = getAccessTokenHeader(accessToken.token), categoryId = categoryId)
                    .map { it.result.playlists.map(PlaylistMapper::mapFrom) }

    companion object {
        fun getAccessTokenHeader(accessToken: String): String = "Bearer $accessToken"

        fun getClientDataHeader(clientId: String, clientSecret: String): String {
            val encoded = Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)
            return "Basic $encoded"
        }
    }
}