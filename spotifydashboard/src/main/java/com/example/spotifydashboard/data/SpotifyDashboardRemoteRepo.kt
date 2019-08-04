package com.example.spotifydashboard.data

import com.example.core.SpotifyDefaults
import com.example.core.model.Resource
import com.example.coreandroid.preferences.SpotifyPreferences
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.SpotifyApi
import com.example.spotifyapi.model.AlbumApiModel
import com.example.spotifyapi.model.PlaylistApiModel
import com.example.spotifyapi.model.TrackApiModel
import com.example.spotifyapi.model.TracksOnlyResponse
import com.example.spotifydashboard.data.api.ChartTrackIdMapper
import com.example.spotifydashboard.data.api.SpotifyDashboardApi
import com.example.spotifydashboard.data.api.SpotifyDashboardChartsApi
import com.example.spotifydashboard.data.api.domain
import com.example.spotifydashboard.data.api.model.CategoryApiModel
import com.example.spotifydashboard.domain.repo.ISpotifyDashboardRemoteRepo
import com.example.spotifyrepo.BaseSpotifyRemoteRepo
import com.example.spotifyrepo.mapper.domain
import com.example.there.domain.entity.ListPage
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.entity.spotify.TopTrackEntity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction


class SpotifyDashboardRemoteRepo(
        preferences: SpotifyPreferences,
        accountsApi: SpotifyAccountsApi,
        private val api: SpotifyDashboardApi,
        private val commonApi: SpotifyApi,
        private val chartsApi: SpotifyDashboardChartsApi
) : BaseSpotifyRemoteRepo(accountsApi, preferences), ISpotifyDashboardRemoteRepo {

    override val categories: Observable<Resource<List<CategoryEntity>>>
        get() = getAllItems { token, offset ->
            api.getCategories(
                    authorization = getAccessTokenHeader(token),
                    offset = offset,
                    country = preferences.country,
                    locale = preferences.locale
            ).toObservable()
        }.mapToResource { items.map(CategoryApiModel::domain) }

    override val featuredPlaylists: Observable<Resource<List<PlaylistEntity>>>
        get() = getAllItems { token, offset ->
            api.getFeaturedPlaylists(
                    authorization = getAccessTokenHeader(token),
                    offset = offset,
                    country = preferences.country,
                    locale = preferences.locale
            ).toObservable()
        }.mapToResource { result.items.map(PlaylistApiModel::domain) }

    override val dailyViralTracks: Observable<Resource<List<TopTrackEntity>>>
        get() = chartsApi.getDailyViralTracks()
                .map { csv -> csv.split('\n').filter { it.isNotBlank() && it.first().isDigit() } }
                .map { it.map(ChartTrackIdMapper::map) }
                .map { it.chunked(50).map { chunk -> chunk.joinToString(",") } }
                .concatMapIterable { it }
                .concatMap { ids ->
                    withTokenObservable {
                        commonApi.getTracks(
                                authorization = getAccessTokenHeader(it),
                                ids = ids
                        ).toObservable()
                    }
                }
                .mapToDataOrThrow { this }
                .zipWith(
                        Observable.range(0, Int.MAX_VALUE),
                        BiFunction<TracksOnlyResponse, Int, Pair<TracksOnlyResponse, Int>> { response, index ->
                            Pair(response, index)
                        }
                )
                .map { (response: TracksOnlyResponse, index: Int) ->
                    Resource.Success(response.tracks.mapIndexed { i: Int, trackData: TrackApiModel ->
                        TopTrackEntity(index * 50 + i + 1, trackData.domain)
                    })
                }

    override fun getNewReleases(
            offset: Int
    ): Single<Resource<ListPage<AlbumEntity>>> = withTokenSingle { token ->
        api.getNewReleases(
                authorization = getAccessTokenHeader(token),
                offset = offset
        ).mapToResource {
            ListPage(
                    items = result.items.map(AlbumApiModel::domain),
                    offset = result.offset + SpotifyDefaults.LIMIT,
                    totalItems = result.totalItems
            )
        }
    }
}