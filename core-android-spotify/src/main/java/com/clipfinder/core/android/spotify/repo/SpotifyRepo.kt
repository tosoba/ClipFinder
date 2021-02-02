package com.clipfinder.core.android.spotify.repo

import com.clipfinder.core.SpotifyDefaults
import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.ext.mapSuccess
import com.clipfinder.core.ext.mapToResource
import com.clipfinder.core.ext.resource
import com.clipfinder.core.ext.toPaged
import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.model.*
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.spotify.api.charts.ChartsEndpoints
import com.clipfinder.spotify.api.endpoint.*
import com.clipfinder.spotify.api.model.*
import io.reactivex.Single

class SpotifyRepo(
    private val preferences: SpotifyPreferences,
    private val albumEndpoints: AlbumEndpoints,
    private val artistEndpoints: ArtistEndpoints,
    private val browseEndpoints: BrowseEndpoints,
    private val chartsEndpoints: ChartsEndpoints,
    private val libraryEndpoints: LibraryEndpoints,
    private val personalizationEndpoints: PersonalizationEndpoints,
    private val playlistsEndpoints: PlaylistsEndpoints,
    private val searchEndpoints: SearchEndpoints,
    private val tracksEndpoints: TracksEndpoints,
    private val userProfileEndpoints: UserProfileEndpoints
) : ISpotifyRepo {

    override val authorizedUser: Single<Resource<ISpotifyPrivateUser>>
        get() = userProfileEndpoints.getCurrentUsersProfile().resource

    override fun getAlbum(id: String): Single<Resource<ISpotifySimplifiedAlbum>> = albumEndpoints
        .getAnAlbum(id = id)
        .resource

    override fun getArtists(ids: List<String>): Single<Resource<List<ISpotifyArtist>>> = artistEndpoints
        .getMultipleArtists(ids = ids.joinToString(separator = ","))
        .mapToResource(ArtistsObject::artists)

    override fun getSimilarTracks(
        id: String, offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = browseEndpoints
        .getRecommendations(seedTracks = id, limit = 100)
        .mapSuccess {
            tracks.chunked(SpotifyDefaults.LIMIT)
                .map { it.joinToString(",", transform = SimplifiedTrackObject::id) }
        }
        .flatMap { chunks ->
            val chunk = chunks[offset]
            tracksEndpoints.getSeveralTracks(ids = chunk)
                .mapToResource { Paged<List<ISpotifyTrack>>(tracks, offset + 1, chunks.size) }
        }

    override fun getAudioFeatures(id: String): Single<Resource<ISpotifyAudioFeatures>> = tracksEndpoints
        .getAudioFeatures(id = id)
        .resource

    override fun getCategories(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifyCategory>>>> = browseEndpoints
        .getCategories(
            offset = offset,
            country = preferences.country,
            locale = preferences.locale
        )
        .mapToResource { categories.toPaged() }

    override fun getFeaturedPlaylists(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>> = browseEndpoints
        .getFeaturedPlaylists(
            offset = offset,
            country = preferences.country,
            locale = preferences.locale
        )
        .mapToResource { playlists.toPaged() }

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
            tracksEndpoints.getSeveralTracks(ids = chunk.joinToString(","))
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
        .mapToResource { albums.toPaged() }

    override fun getPlaylistsForCategory(
        categoryId: String, offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>> = browseEndpoints
        .getACategoriesPlaylists(
            categoryId = categoryId,
            offset = offset,
            country = preferences.country
        )
        .mapToResource { playlists.toPaged() }

    override fun getTracksFromAlbum(
        albumId: String, offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = albumEndpoints
        .getAnAlbumsTracks(id = albumId, offset = offset)
        .mapSuccess {
            Paged(
                contents = items.joinToString(separator = ",", transform = TrackObject::id),
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
        .mapToResource { toPaged() }

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
                albums = album?.toPaged(),
                artists = artist?.toPaged(),
                playlists = playlist?.toPaged(),
                tracks = track?.toPaged()
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

    override fun getCurrentUsersTopTracks(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = personalizationEndpoints
        .getUsersTopTracks(offset = offset)
        .mapToResource { toPaged() }

    override fun getCurrentUsersTopArtists(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifyArtist>>>> = personalizationEndpoints
        .getUsersTopArtists(offset = offset)
        .mapToResource { toPaged() }

    override fun getCurrentUsersSavedTracks(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = libraryEndpoints
        .getUsersSavedTracks(offset = offset)
        .mapToResource { toPaged(mapItems = SavedTrackObject::track) }

    override fun getCurrentUsersSavedAlbums(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>> = libraryEndpoints
        .getUsersSavedAlbums(offset = offset)
        .mapToResource { toPaged(mapItems = SavedAlbumObject::album) }

    override fun getCurrentUsersPlaylists(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>> = playlistsEndpoints
        .getAListOfCurrentUsersPlaylists(offset = offset)
        .mapToResource { toPaged() }
}
