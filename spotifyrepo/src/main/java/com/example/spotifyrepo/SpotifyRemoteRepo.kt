package com.example.spotifyrepo

import android.util.Base64
import com.example.core.model.Resource
import com.example.core.retrofit.NetworkResponse
import com.example.core.retrofit.ThrowableServerError
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.SpotifyApi
import com.example.spotifyapi.SpotifyAuth
import com.example.spotifyapi.SpotifyChartsApi
import com.example.spotifyapi.model.*
import com.example.spotifyrepo.mapper.ChartTrackIdMapper
import com.example.spotifyrepo.mapper.domain
import com.example.spotifyrepo.preferences.SpotifyPreferences
import com.example.spotifyrepo.util.observable
import com.example.spotifyrepo.util.single
import com.example.there.domain.entity.ListPage
import com.example.there.domain.entity.spotify.*
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class SpotifyRemoteRepo @Inject constructor(
        private val api: SpotifyApi,
        private val accountsApi: SpotifyAccountsApi,
        private val chartsApi: SpotifyChartsApi,
        private val preferences: SpotifyPreferences
) : ISpotifyRemoteDataStore {

    private val accessToken: Single<NetworkResponse<AccessTokenApiModel, SpotifyAuthErrorApiModel>>
        get() = accountsApi.getAccessToken(authorization = clientDataHeader)

    override val categories: Observable<Resource<List<CategoryEntity>>>
        get() {
            val offsetSubject = BehaviorSubject.createDefault(0)
            return offsetSubject.concatMap { offset ->
                withTokenObservable {
                    api.getCategories(
                            authorization = getAccessTokenHeader(it),
                            offset = offset,
                            country = preferences.country,
                            locale = preferences.language
                    ).toObservable()
                }
            }.doOnNext {
                if (it is NetworkResponse.Success) {
                    if (it.body.offset < it.body.totalItems - SpotifyApi.DEFAULT_LIMIT)
                        offsetSubject.onNext(it.body.offset + SpotifyApi.DEFAULT_LIMIT)
                    else offsetSubject.onComplete()
                } else offsetSubject.onComplete()
            }.mapToResource { result.categories.map(CategoryApiModel::domain) }
        }

    private fun <T : Any, E : Any, R> Observable<NetworkResponse<T, E>>.mapToResource(
            finisher: T.() -> R
    ): Observable<Resource<R>> = map(resourceMapper(finisher))

    private fun <T : Any, E : Any, R> Single<NetworkResponse<T, E>>.mapToResource(
            finisher: T.() -> R
    ): Single<Resource<R>> = map(resourceMapper(finisher))

    private fun <T : Any, E : Any, R> resourceMapper(
            finisher: T.() -> R
    ): (NetworkResponse<T, E>) -> Resource<R> = { response ->
        when (response) {
            is NetworkResponse.Success -> Resource.Success(response.body.finisher())
            is NetworkResponse.ServerError -> Resource.Error(response.body)
            is NetworkResponse.NetworkError -> Resource.Error(response.error)
        }
    }

    private fun <T : Any, E : Any, R> throwingResourceMapper(
            finisher: T.() -> R
    ): (NetworkResponse<T, E>) -> R = { response ->
        when (response) {
            is NetworkResponse.Success -> Resource.Success(response.body.finisher()).data
            is NetworkResponse.NetworkError -> throw response.error
            is NetworkResponse.ServerError -> throw ThrowableServerError(response)
        }
    }

    private fun <T : Any, E : Any, R> Observable<NetworkResponse<T, E>>.mapToDataOrThrow(
            finisher: T.() -> R
    ): Observable<R> = map(throwingResourceMapper(finisher))

    private fun <T : Any, E : Any, R> Single<NetworkResponse<T, E>>.mapToDataOrThrow(
            finisher: T.() -> R
    ): Single<R> = map(throwingResourceMapper(finisher))

    override val featuredPlaylists: Observable<Resource<List<PlaylistEntity>>>
        get() {
            val offsetSubject = BehaviorSubject.createDefault(0)
            return offsetSubject.concatMap { offset ->
                withTokenObservable {
                    api.getFeaturedPlaylists(
                            authorization = getAccessTokenHeader(it),
                            offset = offset,
                            country = preferences.country,
                            locale = preferences.language
                    ).toObservable()
                }
            }.doOnNext {
                if (it is NetworkResponse.Success) {
                    if (it.body.result.offset < it.body.result.totalItems - SpotifyApi.DEFAULT_LIMIT)
                        offsetSubject.onNext(it.body.result.offset + SpotifyApi.DEFAULT_LIMIT)
                    else offsetSubject.onComplete()
                } else offsetSubject.onComplete()
            }.mapToResource { result.playlists.map(PlaylistApiModel::domain) }
        }

    override val dailyViralTracks: Observable<Resource<List<TopTrackEntity>>>
        get() = chartsApi.getDailyViralTracks()
                .map { csv -> csv.split('\n').filter { it.isNotBlank() && it.first().isDigit() } }
                .map { it.map(ChartTrackIdMapper::map) }
                .map { it.chunked(50).map { chunk -> chunk.joinToString(",") } }
                .concatMapIterable { it }
                .concatMap { ids ->
                    withTokenObservable {
                        api.getTracks(
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

    override val currentUser: Single<Resource<UserEntity>>
        get() = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
            api.getCurrentUser(getAccessTokenHeader(token))
                    .mapToResource(UserApiModel::domain)
        }

    override fun searchAll(
            query: String, offset: Int
    ): Single<Resource<SearchAllEntity>> = withTokenSingle { token ->
        api.searchAll(authorization = getAccessTokenHeader(token), query = query)
                .mapToResource {
                    SearchAllEntity(
                            albums = albumsResult?.albums?.map(AlbumApiModel::domain)
                                    ?: emptyList(),
                            artists = artistsResult?.artists?.map(ArtistApiModel::domain)
                                    ?: emptyList(),
                            playlists = playlistsResult?.playlists?.map(PlaylistApiModel::domain)
                                    ?: emptyList(),
                            tracks = tracksResult?.tracks?.map(TrackApiModel::domain)
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
            categoryId: String, offset: Int
    ): Single<Resource<ListPage<PlaylistEntity>>> = withTokenSingle { token ->
        api.getPlaylistsForCategory(
                authorization = getAccessTokenHeader(token),
                categoryId = categoryId,
                offset = offset,
                country = preferences.country
        ).mapToResource {
            ListPage(
                    items = result.playlists.map(PlaylistApiModel::domain),
                    offset = result.offset,
                    totalItems = result.totalItems
            )
        }
    }

    override fun getPlaylistTracks(
            playlistId: String, userId: String, offset: Int
    ): Single<Resource<ListPage<TrackEntity>>> = withTokenSingle { token ->
        api.getPlaylistTracks(
                authorization = getAccessTokenHeader(token),
                playlistId = playlistId,
                userId = userId,
                offset = offset
        ).mapToResource {
            ListPage(
                    items = playlistTracks.map { it.track.domain },
                    offset = offset,
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

    override fun getAlbumsFromArtist(artistId: String): Observable<Resource<List<AlbumEntity>>> {
        val offsetSubject = BehaviorSubject.createDefault(0)
        return offsetSubject.concatMap { offset ->
            withTokenObservable { token ->
                api.getAlbumsFromArtist(
                        authorization = getAccessTokenHeader(token),
                        artistId = artistId,
                        offset = offset
                ).toObservable()
            }
        }.doOnNext {
            if (it is NetworkResponse.Success) {
                if (it.body.offset < it.body.totalItems - SpotifyApi.DEFAULT_LIMIT)
                    offsetSubject.onNext(it.body.offset + SpotifyApi.DEFAULT_LIMIT)
                else offsetSubject.onComplete()
            } else offsetSubject.onComplete()
        }.mapToResource { albums.map(AlbumApiModel::domain) }
    }

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
            albumId: String
    ): Observable<Resource<ListPage<TrackEntity>>> {
        val offsetSubject = BehaviorSubject.createDefault(0)
        return offsetSubject.concatMap { offset ->
            withTokenObservable { token ->
                api.getTracksFromAlbum(
                        authorization = getAccessTokenHeader(token),
                        albumId = albumId,
                        offset = offset
                ).toObservable().mapToDataOrThrow {
                    TrackIdsPage(
                            ids = tracks.joinToString(separator = ",") { it.id },
                            offset = offset,
                            totalItems = totalItems)
                }.flatMap { idsPage ->
                    api.getTracks(
                            authorization = getAccessTokenHeader(token),
                            ids = idsPage.ids
                    ).toObservable().mapToDataOrThrow {
                        ListPage(
                                items = tracks.map(TrackApiModel::domain),
                                offset = idsPage.offset,
                                totalItems = idsPage.totalItems
                        )
                    }
                }
            }.doOnNext { entityPage ->
                if (entityPage.offset < entityPage.totalItems - SpotifyApi.DEFAULT_LIMIT)
                    offsetSubject.onNext(entityPage.offset + SpotifyApi.DEFAULT_LIMIT)
                else offsetSubject.onComplete()
            }.map<Resource<ListPage<TrackEntity>>> {
                Resource.Success(it)
            }.onErrorResumeNext { throwable: Throwable ->
                when (throwable) {
                    is ThrowableServerError -> Observable.just(Resource.Error(throwable.error))
                    else -> Observable.just(Resource.Error(throwable))
                }
            }
        }

    }

    override fun getNewReleases(
            offset: Int
    ): Single<Resource<ListPage<AlbumEntity>>> = withTokenSingle { token ->
        api.getNewReleases(
                authorization = getAccessTokenHeader(token),
                offset = offset
        ).mapToResource {
            ListPage(
                    items = result.albums.map(AlbumApiModel::domain),
                    offset = result.offset,
                    totalItems = result.totalItems
            )
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
                    items = playlists.map(PlaylistApiModel::domain),
                    offset = offset,
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
                    items = tracks.map(TrackApiModel::domain),
                    offset = offset,
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
                    items = artists.map(ArtistApiModel::domain),
                    offset = offset,
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
                    items = savedTracks.map { it.track.domain },
                    offset = offset,
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
                    items = savedAlbums.map { it.album.domain },
                    offset = offset,
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

    private fun <T> withTokenObservable(
            block: (String) -> Observable<T>
    ): Observable<T> = preferences.accessToken.observable.loadIfNeededThenFlatMapValid(block)

    private fun <T> withTokenSingle(
            block: (String) -> Single<T>
    ): Single<T> = preferences.accessToken.single.loadIfNeededThenFlatMapValid(block)

    private fun <T> Observable<SpotifyPreferences.SavedAccessTokenEntity>.loadIfNeededThenFlatMapValid(
            block: (String) -> Observable<T>
    ): Observable<T> = flatMap { saved ->
        when (saved) {
            is SpotifyPreferences.SavedAccessTokenEntity.Valid -> block(saved.token)
            else -> accessToken.toObservable()
                    .mapToDataOrThrow(AccessTokenApiModel::domain)
                    .doOnNext { preferences.accessToken = it }
                    .map { it.token }
                    .flatMap(block)
        }
    }

    private fun <T> Observable<SpotifyPreferences.SavedAccessTokenEntity>.flatMapValidElseThrow(
            block: (String) -> Observable<T>
    ): Observable<T> = flatMap { saved ->
        when (saved) {
            is SpotifyPreferences.SavedAccessTokenEntity.Valid -> block(saved.token)
            else -> Observable.error { IllegalStateException(ACCESS_TOKEN_UNAVAILABLE) }
        }
    }

    private fun <T> Single<SpotifyPreferences.SavedAccessTokenEntity>.loadIfNeededThenFlatMapValid(
            block: (String) -> Single<T>
    ): Single<T> = flatMap { saved ->
        when (saved) {
            is SpotifyPreferences.SavedAccessTokenEntity.Valid -> block(saved.token)
            else -> accessToken
                    .mapToDataOrThrow(AccessTokenApiModel::domain)
                    .doOnSuccess { preferences.accessToken = it }
                    .map { it.token }
                    .flatMap(block)
        }
    }

    private fun <T> Single<SpotifyPreferences.SavedAccessTokenEntity>.flatMapValidElseThrow(
            block: (String) -> Single<T>
    ): Single<T> = flatMap { saved ->
        when (saved) {
            is SpotifyPreferences.SavedAccessTokenEntity.Valid -> block(saved.token)
            else -> Single.error { IllegalStateException(ACCESS_TOKEN_UNAVAILABLE) }
        }
    }

    companion object {
        private fun getAccessTokenHeader(accessToken: String): String = "Bearer $accessToken"

        private const val ACCESS_TOKEN_UNAVAILABLE = "Access token unavailable."

        val clientDataHeader: String
            get() {
                val encoded = Base64.encodeToString("${SpotifyAuth.id}:${SpotifyAuth.secret}".toByteArray(), Base64.NO_WRAP)
                return "Basic $encoded"
            }
    }
}