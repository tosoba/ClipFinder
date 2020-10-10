package com.example.spotifyrepo

import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.model.ext.single
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.Resource
import com.example.core.retrofit.ThrowableServerError
import com.example.core.retrofit.mapSuccess
import com.example.core.retrofit.mapToResource
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.SpotifyApi
import com.example.spotifyapi.model.*
import com.example.spotifyrepo.mapper.domain
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.*
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import io.reactivex.Observable
import io.reactivex.Single

class SpotifyRemoteRepo(
    preferences: SpotifyPreferences,
    accountsApi: SpotifyAccountsApi,
    private val api: SpotifyApi
) : BaseSpotifyRemoteRepo(accountsApi, preferences), ISpotifyRemoteDataStore {

    override val currentUser: Single<Resource<UserEntity>>
        get() = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
            api.getCurrentUser(getAccessTokenHeader(token))
                .mapToResource(UserApiModel::domain)
        }

    override fun getPlaylistsForCategory(
        categoryId: String,
        offset: Int
    ): Single<Resource<Page<PlaylistEntity>>> = withTokenSingle { token ->
        api.getPlaylistsForCategory(
            authorization = getAccessTokenHeader(token),
            categoryId = categoryId,
            offset = offset,
            country = preferences.country
        ).mapToResource {
            Page(
                items = result.items.map(PlaylistApiModel::domain),
                offset = result.offset + SpotifyDefaults.LIMIT,
                total = result.totalItems
            )
        }
    }

    override fun getPlaylistTracks(
        playlistId: String,
        userId: String,
        offset: Int
    ): Single<Resource<Page<TrackEntity>>> = withTokenSingle { token ->
        api.getPlaylistTracks(
            authorization = getAccessTokenHeader(token),
            playlistId = playlistId,
            userId = userId,
            offset = offset
        ).mapToResource {
            Page(
                items = items.map { it.track.domain },
                offset = offset + SpotifyDefaults.LIMIT,
                total = totalItems
            )
        }
    }

    override fun getAlbum(albumId: String): Single<Resource<AlbumEntity>> = withTokenSingle { token ->
        api.getAlbum(
            authorization = getAccessTokenHeader(token),
            albumId = albumId
        ).mapToResource(AlbumApiModel::domain)
    }

    override fun getArtists(
        artistIds: List<String>
    ): Single<Resource<List<ArtistEntity>>> = withTokenSingle { token ->
        api.getArtists(
            authorization = getAccessTokenHeader(token),
            artistIds = artistIds.joinToString(",")
        ).mapToResource { artists.map(ArtistApiModel::domain) }
    }

    override fun getSimilarTracks(
        trackId: String
    ): Observable<Resource<List<TrackEntity>>> = withTokenObservable { token ->
        api.getSimilarTracks(authorization = getAccessTokenHeader(token), trackId = trackId)
            .mapSuccess {
                tracks.chunked(50).map {
                    it.joinToString(",") { track -> track.id }
                }
            }
            .toObservable()
            .flatMapIterable { it }
            .flatMap {
                api.getTracks(
                    authorization = getAccessTokenHeader(token),
                    ids = it
                ).toObservable()
            }
            .mapToResource { tracks.map(TrackApiModel::domain) }
            .onErrorResumeNext { throwable: Throwable ->
                when (throwable) {
                    is ThrowableServerError -> Observable.just(Resource.Error(throwable.error))
                    else -> Observable.just(Resource.Error(throwable))
                }
            }
    }

    override fun getAlbumsFromArtist(
        artistId: String
    ): Observable<Resource<List<AlbumEntity>>> = getAllItems { token, offset ->
        api.getAlbumsFromArtist(
            authorization = getAccessTokenHeader(token),
            artistId = artistId,
            offset = offset
        ).toObservable()
    }.mapToResource { items.map(AlbumApiModel::domain) }


    override fun getTopTracksFromArtist(
        artistId: String
    ): Single<Resource<List<TrackEntity>>> = withTokenSingle { token ->
        api.getTopTracksFromArtist(
            authorization = getAccessTokenHeader(token),
            artistId = artistId,
            country = preferences.country
        ).mapToResource { tracks.map(TrackApiModel::domain) }
    }

    override fun getRelatedArtists(
        artistId: String
    ): Single<Resource<List<ArtistEntity>>> = withTokenSingle { token ->
        api.getRelatedArtists(
            authorization = getAccessTokenHeader(token),
            artistId = artistId
        ).mapToResource { artists.map(ArtistApiModel::domain) }
    }

    private class TrackIdsPage(
        val ids: String,
        val offset: Int,
        val totalItems: Int
    )

    override fun getTracksFromAlbum(
        albumId: String, offset: Int
    ): Single<Resource<Page<TrackEntity>>> = withTokenSingle { token ->
        api.getTracksFromAlbum(
            authorization = getAccessTokenHeader(token),
            albumId = albumId,
            offset = offset
        ).mapSuccess {
            TrackIdsPage(
                ids = items.joinToString(separator = ",") { it.id },
                offset = offset,
                totalItems = totalItems
            )
        }.flatMap { idsPage ->
            api.getTracks(
                authorization = getAccessTokenHeader(token),
                ids = idsPage.ids
            ).mapToResource {
                Page(
                    items = tracks.map(TrackApiModel::domain),
                    offset = idsPage.offset + SpotifyDefaults.LIMIT,
                    total = idsPage.totalItems
                )
            }
        }
    }

    override fun getCurrentUsersPlaylists(
        offset: Int
    ): Single<Resource<Page<PlaylistEntity>>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersPlaylists(
            authorization = getAccessTokenHeader(token),
            offset = offset
        ).mapToResource {
            Page(
                items = items.map(PlaylistApiModel::domain),
                offset = offset + SpotifyDefaults.LIMIT,
                total = totalItems
            )
        }
    }

    override fun getCurrentUsersTopTracks(
        offset: Int
    ): Single<Resource<Page<TrackEntity>>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersTopTracks(
            authorization = getAccessTokenHeader(token),
            offset = offset
        ).mapToResource {
            Page(
                items = items.map(TrackApiModel::domain),
                offset = offset + SpotifyDefaults.LIMIT,
                total = totalItems
            )
        }
    }

    override fun getCurrentUsersTopArtists(
        offset: Int
    ): Single<Resource<Page<ArtistEntity>>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersTopArtists(
            authorization = getAccessTokenHeader(token),
            offset = offset
        ).mapToResource {
            Page(
                items = items.map(ArtistApiModel::domain),
                offset = offset + SpotifyDefaults.LIMIT,
                total = totalItems
            )
        }
    }

    override fun getCurrentUsersSavedTracks(
        offset: Int
    ): Single<Resource<Page<TrackEntity>>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersSavedTracks(
            authorization = getAccessTokenHeader(token),
            offset = offset
        ).mapToResource {
            Page(
                items = items.map { it.track.domain },
                offset = offset + SpotifyDefaults.LIMIT,
                total = totalItems
            )
        }
    }

    override fun getCurrentUsersSavedAlbums(
        offset: Int
    ): Single<Resource<Page<AlbumEntity>>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersSavedAlbums(
            authorization = getAccessTokenHeader(token),
            offset = offset
        ).mapToResource {
            Page(
                items = items.map { it.album.domain },
                offset = offset + SpotifyDefaults.LIMIT,
                total = totalItems
            )
        }
    }

    override fun getAudioFeatures(
        trackEntity: TrackEntity
    ): Single<Resource<AudioFeaturesEntity>> = withTokenSingle { token ->
        api.getAudioFeatures(
            authorization = getAccessTokenHeader(token),
            trackId = trackEntity.id
        ).mapToResource(AudioFeaturesApiModel::domain)
    }
}
