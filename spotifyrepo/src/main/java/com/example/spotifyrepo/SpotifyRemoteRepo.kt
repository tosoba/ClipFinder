package com.example.spotifyrepo

import com.example.core.SpotifyDefaults
import com.example.core.model.Resource
import com.example.core.retrofit.ThrowableServerError
import com.example.core.retrofit.mapToDataOrThrow
import com.example.core.retrofit.mapToResource
import com.example.coreandroid.preferences.SpotifyPreferences
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.SpotifyApi
import com.example.spotifyapi.model.*
import com.example.spotifyrepo.mapper.domain
import com.example.spotifyrepo.util.single
import com.example.there.domain.entity.ListPage
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

    override fun searchAll(
        query: String,
        offset: Int
    ): Single<Resource<SearchAllEntity>> = withTokenSingle { token ->
        api.searchAll(authorization = getAccessTokenHeader(token), query = query, offset = offset)
            .mapToResource {
                SearchAllEntity(
                    albums = albumsResult?.items?.map(AlbumApiModel::domain)
                        ?: emptyList(),
                    artists = artistsResult?.items?.map(ArtistApiModel::domain)
                        ?: emptyList(),
                    playlists = playlistsResult?.items?.map(PlaylistApiModel::domain)
                        ?: emptyList(),
                    tracks = tracksResult?.items?.map(TrackApiModel::domain)
                        ?: emptyList(),
                    totalItems = arrayOf(
                        albumsResult?.totalItems ?: 0,
                        artistsResult?.totalItems ?: 0,
                        playlistsResult?.totalItems ?: 0,
                        tracksResult?.totalItems ?: 0
                    ).max() ?: 0
                )
            }
    }

    override fun getPlaylistsForCategory(
        categoryId: String,
        offset: Int
    ): Single<Resource<ListPage<PlaylistEntity>>> = withTokenSingle { token ->
        api.getPlaylistsForCategory(
            authorization = getAccessTokenHeader(token),
            categoryId = categoryId,
            offset = offset,
            country = preferences.country
        ).mapToResource {
            ListPage(
                items = result.items.map(PlaylistApiModel::domain),
                offset = result.offset + SpotifyDefaults.LIMIT,
                totalItems = result.totalItems
            )
        }
    }

    override fun getPlaylistTracks(
        playlistId: String,
        userId: String,
        offset: Int
    ): Single<Resource<ListPage<TrackEntity>>> = withTokenSingle { token ->
        api.getPlaylistTracks(
            authorization = getAccessTokenHeader(token),
            playlistId = playlistId,
            userId = userId,
            offset = offset
        ).mapToResource {
            ListPage(
                items = items.map { it.track.domain },
                offset = offset + SpotifyDefaults.LIMIT,
                totalItems = totalItems
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
            .mapToDataOrThrow {
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
    ): Single<Resource<ListPage<TrackEntity>>> = withTokenSingle { token ->
        api.getTracksFromAlbum(
            authorization = getAccessTokenHeader(token),
            albumId = albumId,
            offset = offset
        ).mapToDataOrThrow {
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
                ListPage(
                    items = tracks.map(TrackApiModel::domain),
                    offset = idsPage.offset + SpotifyDefaults.LIMIT,
                    totalItems = idsPage.totalItems
                )
            }
        }
    }

    override fun getCurrentUsersPlaylists(
        offset: Int
    ): Single<Resource<ListPage<PlaylistEntity>>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersPlaylists(
            authorization = getAccessTokenHeader(token),
            offset = offset
        ).mapToResource {
            ListPage(
                items = items.map(PlaylistApiModel::domain),
                offset = offset + SpotifyDefaults.LIMIT,
                totalItems = totalItems
            )
        }
    }

    override fun getCurrentUsersTopTracks(
        offset: Int
    ): Single<Resource<ListPage<TrackEntity>>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersTopTracks(
            authorization = getAccessTokenHeader(token),
            offset = offset
        ).mapToResource {
            ListPage(
                items = items.map(TrackApiModel::domain),
                offset = offset + SpotifyDefaults.LIMIT,
                totalItems = totalItems
            )
        }
    }

    override fun getCurrentUsersTopArtists(
        offset: Int
    ): Single<Resource<ListPage<ArtistEntity>>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersTopArtists(
            authorization = getAccessTokenHeader(token),
            offset = offset
        ).mapToResource {
            ListPage(
                items = items.map(ArtistApiModel::domain),
                offset = offset + SpotifyDefaults.LIMIT,
                totalItems = totalItems
            )
        }
    }

    override fun getCurrentUsersSavedTracks(
        offset: Int
    ): Single<Resource<ListPage<TrackEntity>>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersSavedTracks(
            authorization = getAccessTokenHeader(token),
            offset = offset
        ).mapToResource {
            ListPage(
                items = items.map { it.track.domain },
                offset = offset + SpotifyDefaults.LIMIT,
                totalItems = totalItems
            )
        }
    }

    override fun getCurrentUsersSavedAlbums(
        offset: Int
    ): Single<Resource<ListPage<AlbumEntity>>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersSavedAlbums(
            authorization = getAccessTokenHeader(token),
            offset = offset
        ).mapToResource {
            ListPage(
                items = items.map { it.album.domain },
                offset = offset + SpotifyDefaults.LIMIT,
                totalItems = totalItems
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
