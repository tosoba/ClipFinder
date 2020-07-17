package com.example.spotifydashboard.data

import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.SpotifyBrowseApi
import com.example.spotifyapi.SpotifyChartsApi
import com.example.spotifyapi.SpotifyTracksApi
import com.example.spotifyapi.models.SimpleAlbum
import com.example.spotifyapi.models.SimplePlaylist
import com.example.spotifyapi.models.SpotifyCategory
import com.example.spotifyapi.models.Track
import com.example.spotifyapi.util.ChartTrackIdMapper
import com.example.spotifyapi.util.domain
import com.example.spotifydashboard.domain.repo.ISpotifyDashboardRepo
import com.example.spotifyrepo.BaseSpotifyRemoteRepo
import com.example.there.domain.entity.ListPage
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.entity.spotify.TopTrackEntity
import io.reactivex.Single

class SpotifyDashboardRepo(
    preferences: SpotifyPreferences,
    accountsApi: SpotifyAccountsApi,
    private val tracksApi: SpotifyTracksApi,
    private val chartsApi: SpotifyChartsApi,
    private val browseApi: SpotifyBrowseApi
) : BaseSpotifyRemoteRepo(accountsApi, preferences), ISpotifyDashboardRepo {

    override fun getCategories(
        offset: Int
    ): Single<Resource<ListPage<CategoryEntity>>> = withTokenSingle { token ->
        browseApi.getCategories(
            authorization = getAccessTokenHeader(token),
            offset = offset,
            country = preferences.country,
            locale = preferences.locale
        ).mapToResource {
            ListPage(
                items = items.map(SpotifyCategory::domain),
                offset = result.offset + SpotifyDefaults.LIMIT,
                totalItems = result.total
            )
        }
    }

    override fun getFeaturedPlaylists(
        offset: Int
    ): Single<Resource<ListPage<PlaylistEntity>>> = withTokenSingle { token ->
        browseApi.getFeaturedPlaylists(
            authorization = getAccessTokenHeader(token),
            offset = offset,
            country = preferences.country,
            locale = preferences.locale
        )
    }.mapToResource {
        ListPage(
            items = items.map(SimplePlaylist::domain),
            offset = playlists.offset + SpotifyDefaults.LIMIT,
            totalItems = playlists.total
        )
    }

    override fun getDailyViralTracks(
        offset: Int
    ): Single<Resource<ListPage<TopTrackEntity>>> = chartsApi.getDailyViralTracks()
        .map { csv ->
            csv.split('\n')
                .filter { it.isNotBlank() && it.first().isDigit() }
                .map(ChartTrackIdMapper::map)
                .chunked(SpotifyDefaults.LIMIT)
        }
        .flatMap { chunks ->
            val chunk = chunks[offset]
            val ids = chunk.joinToString(",")
            withTokenSingle { token ->
                tracksApi.getTracks(
                    authorization = getAccessTokenHeader(token),
                    ids = ids
                )
            }.mapToResource {
                ListPage(
                    items = result.mapIndexed { index: Int, track: Track ->
                        TopTrackEntity(
                            offset * SpotifyDefaults.LIMIT + index + 1,
                            track.domain
                        )
                    },
                    offset = offset + 1,
                    totalItems = chunks.size
                )
            }
        }


    override fun getNewReleases(
        offset: Int
    ): Single<Resource<ListPage<AlbumEntity>>> = withTokenSingle { token ->
        browseApi.getNewReleases(
            authorization = getAccessTokenHeader(token),
            offset = offset
        ).mapToResource {
            ListPage(
                items = result.items.map(SimpleAlbum::domain),
                offset = result.offset + SpotifyDefaults.LIMIT,
                totalItems = result.total
            )
        }
    }
}
