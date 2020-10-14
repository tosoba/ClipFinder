package com.example.spotify.dashboard.data

import com.clipfinder.core.spotify.model.ISpotifyCategory
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.model.ISpotifySimplifiedPlaylist
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.spotify.api.endpoint.BrowseEndpoints
import com.clipfinder.spotify.api.endpoint.TracksEndpoints
import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotify.dashboard.domain.repo.ISpotifyDashboardRepo
import com.example.spotifyapi.SpotifyChartsApi
import io.reactivex.Single

class SpotifyDashboardRepo(
    private val preferences: SpotifyPreferences,
    private val tracksEndpoints: TracksEndpoints,
    private val chartsApi: SpotifyChartsApi,
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
    ): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>> = browseEndpoints.getFeaturedPlaylists(
        offset = offset,
        country = preferences.country,
        locale = preferences.locale
    ).mapToResource {
        Paged<List<ISpotifySimplifiedPlaylist>>(
            contents = playlists.items,
            offset = playlists.offset + SpotifyDefaults.LIMIT,
            total = playlists.total
        )
    }

    override fun getDailyViralTracks(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = chartsApi.getDailyViralTracks()
        .map { csv ->
            csv.split('\n')
                .filter { line -> line.isNotBlank() && line.first().isDigit() }
                .map { line -> line.substring(line.lastIndexOf('/') + 1) }
                .chunked(SpotifyDefaults.LIMIT)
        }
        .flatMap { chunks ->
            val chunk = chunks[offset]
            val trackIds = chunk.joinToString(",")
            tracksEndpoints.getSeveralTracks(ids = trackIds)
                .mapToResource {
                    Paged<List<ISpotifyTrack>>(
                        contents = tracks,
                        offset = offset + 1,
                        total = chunks.size
                    )
                }
        }

    override fun getNewReleases(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>> = browseEndpoints
        .getNewReleases(offset = offset)
        .mapToResource {
            Paged<List<ISpotifySimplifiedAlbum>>(
                contents = albums.items,
                offset = offset + SpotifyDefaults.LIMIT,
                total = albums.total
            )
        }
}
