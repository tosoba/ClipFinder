package com.example.spotify.dashboard.data

import com.clipfinder.core.spotify.model.ISpotifyCategory
import com.clipfinder.core.spotify.model.ISpotifySimplePlaylist
import com.clipfinder.spotify.api.endpoint.BrowseEndpoints
import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotify.dashboard.domain.repo.ISpotifyDashboardRepo
import com.example.spotifyapi.SpotifyBrowseApi
import com.example.spotifyapi.SpotifyChartsApi
import com.example.spotifyapi.SpotifyTracksApi
import com.example.spotifyapi.models.SimpleAlbum
import com.example.spotifyapi.models.Track
import com.example.spotifyapi.util.domain
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.TopTrackEntity
import io.reactivex.Single

class SpotifyDashboardRepo(
    private val preferences: SpotifyPreferences,
    private val auth: SpotifyAuth,
    private val tracksApi: SpotifyTracksApi,
    private val chartsApi: SpotifyChartsApi,
    private val browseApi: SpotifyBrowseApi,
    private val browseEndpoints: BrowseEndpoints
) : ISpotifyDashboardRepo {

    override fun getCategories(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifyCategory>>>> = browseEndpoints.getCategories(
        offset = offset,
        country = preferences.country,
        locale = preferences.locale
    ).mapToResource {
        Paged<List<ISpotifyCategory>>(
            contents = categories.items,
            offset = categories.offset + SpotifyDefaults.LIMIT,
            total = categories.total
        )
    }

    override fun getFeaturedPlaylists(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplePlaylist>>>> = browseEndpoints.getFeaturedPlaylists(
        offset = offset,
        country = preferences.country,
        locale = preferences.locale
    ).mapToResource {
        Paged<List<ISpotifySimplePlaylist>>(
            contents = playlists.items,
            offset = playlists.offset + SpotifyDefaults.LIMIT,
            total = playlists.total
        )
    }

    override fun getDailyViralTracks(
        offset: Int
    ): Single<Resource<Paged<List<TopTrackEntity>>>> = chartsApi.getDailyViralTracks()
        .map { csv ->
            csv.split('\n')
                .filter { line -> line.isNotBlank() && line.first().isDigit() }
                .map { line -> line.substring(line.lastIndexOf('/') + 1) }
                .chunked(SpotifyDefaults.LIMIT)
        }
        .flatMap { chunks ->
            val chunk = chunks[offset]
            val trackIds = chunk.joinToString(",")
            auth.withTokenSingle { token ->
                tracksApi.getTracks(authorization = token, ids = trackIds)
            }.mapToResource {
                Paged(
                    contents = result.mapIndexed { index: Int, track: Track ->
                        TopTrackEntity(
                            offset * SpotifyDefaults.LIMIT + index + 1,
                            track.domain
                        )
                    },
                    offset = offset + 1,
                    total = chunks.size
                )
            }
        }

    override fun getNewReleases(
        offset: Int
    ): Single<Resource<Paged<List<AlbumEntity>>>> = auth.withTokenSingle { token ->
        browseApi.getNewReleases(
            authorization = token,
            offset = offset
        ).mapToResource {
            Paged(
                contents = result.items.map(SimpleAlbum::domain),
                offset = result.offset + SpotifyDefaults.LIMIT,
                total = result.total
            )
        }
    }
}
