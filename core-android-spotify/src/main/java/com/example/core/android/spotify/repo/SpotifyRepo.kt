package com.example.core.android.spotify.repo

import com.clipfinder.core.spotify.model.*
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.spotify.api.charts.ChartsEndpoints
import com.clipfinder.spotify.api.endpoint.*
import com.clipfinder.spotify.api.model.*
import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapSuccess
import com.example.core.retrofit.mapToResource
import io.reactivex.Single

class SpotifyRepo(
    private val preferences: SpotifyPreferences,
    private val albumEndpoints: AlbumEndpoints,
    private val artistEndpoints: ArtistEndpoints,
    private val browseEndpoints: BrowseEndpoints,
    private val chartsEndpoints: ChartsEndpoints,
    private val playlistsEndpoints: PlaylistsEndpoints,
    private val searchEndpoints: SearchEndpoints,
    private val tracksEndpoints: TracksEndpoints,
    private val userProfileEndpoints: UserProfileEndpoints
) : ISpotifyRepo {

    override val authorizedUser: Single<Resource<ISpotifyPrivateUser>>
        get() = userProfileEndpoints.getCurrentUsersProfile().mapToResource { this }

    override fun getAlbum(id: String): Single<Resource<ISpotifySimplifiedAlbum>> = albumEndpoints
        .getAnAlbum(id = id)
        .mapToResource { this }

    override fun getArtists(ids: List<String>): Single<Resource<List<ISpotifyArtist>>> = artistEndpoints
        .getMultipleArtists(ids = ids.joinToString(separator = ","))
        .mapToResource(ArtistsObject::artists)

    override fun getSimilarTracks(
        id: String, offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = browseEndpoints
        .getRecommendations(seedTracks = id, limit = 100)
        .mapSuccess {
            tracks.chunked(SpotifyDefaults.LIMIT)
                .map { it.joinToString(",") { track -> track.id } }
        }
        .flatMap { chunks ->
            val chunk = chunks[offset]
            tracksEndpoints.getSeveralTracks(ids = chunk)
                .mapToResource { Paged<List<ISpotifyTrack>>(tracks, offset + 1, chunks.size) }
        }

    override fun getAudioFeatures(id: String): Single<Resource<ISpotifyAudioFeatures>> = tracksEndpoints
        .getAudioFeatures(id = id)
        .mapToResource { this }

    override fun getCategories(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifyCategory>>>> = browseEndpoints
        .getCategories(
            offset = offset,
            country = preferences.country,
            locale = preferences.locale
        )
        .mapToResource {
            Paged<List<ISpotifyCategory>>(
                contents = categories.items,
                offset = categories.offset + SpotifyDefaults.LIMIT,
                total = categories.total
            )
        }

    override fun getFeaturedPlaylists(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>> = browseEndpoints
        .getFeaturedPlaylists(
            offset = offset,
            country = preferences.country,
            locale = preferences.locale
        )
        .mapToResource {
            Paged<List<ISpotifySimplifiedPlaylist>>(
                contents = playlists.items,
                offset = playlists.offset + SpotifyDefaults.LIMIT,
                total = playlists.total
            )
        }

    override fun getDailyViralTracks(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = chartsEndpoints
        .getDailyViralTracks()
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

    override fun getPlaylistsForCategory(
        categoryId: String, offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>> = browseEndpoints
        .getACategoriesPlaylists(
            categoryId = categoryId,
            offset = offset,
            country = preferences.country
        )
        .mapToResource {
            Paged<List<ISpotifySimplifiedPlaylist>>(
                contents = playlists.items,
                offset = playlists.offset + SpotifyDefaults.LIMIT,
                total = playlists.total
            )
        }

    override fun getTracksFromAlbum(
        albumId: String, offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = albumEndpoints
        .getAnAlbumsTracks(id = albumId, offset = offset)
        .mapSuccess {
            Paged(
                contents = items.joinToString(separator = ",") { it.id },
                offset = offset,
                total = total
            )
        }
        .flatMap { idsPage ->
            tracksEndpoints
                .getSeveralTracks(ids = idsPage.contents)
                .mapToResource {
                    Paged<List<ISpotifyTrack>>(
                        contents = tracks,
                        offset = idsPage.offset + SpotifyDefaults.LIMIT,
                        total = idsPage.total
                    )
                }
        }

    override fun getAlbumsFromArtist(
        artistId: String, offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>> = artistEndpoints
        .getAnArtistsAlbums(id = artistId, offset = offset)
        .mapToResource {
            Paged<List<ISpotifySimplifiedAlbum>>(
                contents = items,
                offset = this.offset + SpotifyDefaults.LIMIT,
                total = total
            )
        }

    override fun getRelatedArtists(
        artistId: String
    ): Single<Resource<List<ISpotifyArtist>>> = artistEndpoints
        .getAnArtistsRelatedArtists(id = artistId)
        .mapToResource(ArtistsObject::artists)

    override fun getTopTracksFromArtist(
        artistId: String
    ): Single<Resource<List<ISpotifyTrack>>> = artistEndpoints
        .getAnArtistsTopTracks(id = artistId, market = preferences.country)
        .mapToResource(TracksObject::tracks)

    override fun search(
        query: String, offset: Int, type: String
    ): Single<Resource<SpotifySearchResult>> = searchEndpoints
        .search(q = query, offset = offset, type = type)
        .mapToResource {
            SpotifySearchResult(
                albums = album?.let {
                    Paged<List<ISpotifySimplifiedAlbum>>(it.items, it.offset + SpotifyDefaults.LIMIT, it.total)
                },
                artists = artist?.let {
                    Paged<List<ISpotifyArtist>>(it.items, it.offset + SpotifyDefaults.LIMIT, it.total)
                },
                playlists = playlist?.let {
                    Paged<List<ISpotifySimplifiedPlaylist>>(it.items, it.offset + SpotifyDefaults.LIMIT, it.total)
                },
                tracks = track?.let {
                    Paged<List<ISpotifyTrack>>(it.items, it.offset + SpotifyDefaults.LIMIT, it.total)
                }
            )
        }

    override fun getPlaylistTracks(
        playlistId: String, offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = playlistsEndpoints
        .getPlaylistsTracks(playlistId = playlistId, offset = offset)
        .mapToResource {
            Paged<List<ISpotifyTrack>>(
                contents = items.map(PlaylistItemObjectWrapper::track).filterIsInstance<TrackObject>(),
                offset = offset + SpotifyDefaults.LIMIT,
                total = total
            )
        }
}