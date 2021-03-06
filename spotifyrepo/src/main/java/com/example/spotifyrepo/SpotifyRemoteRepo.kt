package com.example.spotifyrepo

import android.util.Base64
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.SpotifyApi
import com.example.spotifyapi.SpotifyAuth
import com.example.spotifyapi.SpotifyChartsApi
import com.example.spotifyapi.model.*
import com.example.spotifyrepo.mapper.domain
import com.example.spotifyrepo.preferences.SpotifyPreferences
import com.example.spotifyrepo.util.observable
import com.example.spotifyrepo.util.single
import com.example.there.domain.entity.EntityPage
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

    private val accessToken: Single<AccessTokenEntity>
        get() = accountsApi.getAccessToken(authorization = clientDataHeader)
                .map(AccessTokenApiModel::domain)

    override val categories: Observable<List<CategoryEntity>>
        get() {
            val offsetSubject = BehaviorSubject.createDefault(0)
            return offsetSubject.concatMap { offset ->
                preferences.accessToken.observable.loadIfNeededThenFlatMapValid {
                    api.getCategories(
                            authorization = getAccessTokenHeader(it),
                            offset = offset,
                            country = preferences.country,
                            locale = preferences.language
                    ).toObservable()
                }
            }.doOnNext {
                if (it.offset < it.totalItems - SpotifyApi.DEFAULT_LIMIT)
                    offsetSubject.onNext(it.offset + SpotifyApi.DEFAULT_LIMIT)
                else offsetSubject.onComplete()
            }.map { it.result.categories.map(CategoryApiModel::domain) }
        }

    override val featuredPlaylists: Observable<List<PlaylistEntity>>
        get() {
            val offsetSubject = BehaviorSubject.createDefault(0)
            return offsetSubject.concatMap { offset ->
                preferences.accessToken.observable.loadIfNeededThenFlatMapValid {
                    api.getFeaturedPlaylists(
                            authorization = getAccessTokenHeader(it),
                            offset = offset,
                            country = preferences.country,
                            locale = preferences.language
                    ).toObservable()
                }
            }.doOnNext {
                if (it.result.offset < it.result.totalItems - SpotifyApi.DEFAULT_LIMIT)
                    offsetSubject.onNext(it.result.offset + SpotifyApi.DEFAULT_LIMIT)
                else
                    offsetSubject.onComplete()
            }.map { it.result.playlists.map(PlaylistApiModel::domain) }
        }

    override val dailyViralTracks: Observable<List<TopTrackEntity>>
        get() = chartsApi.getDailyViralTracks()
                .map { csv -> csv.split('\n').filter { it.isNotBlank() && it.first().isDigit() } }
                .map { it.map(com.example.spotifyrepo.mapper.ChartTrackIdMapper::map) }
                .map { it.chunked(50).map { chunk -> chunk.joinToString(",") } }
                .concatMapIterable { it }
                .concatMap { ids ->
                    preferences.accessToken.observable.loadIfNeededThenFlatMapValid {
                        api.getTracks(
                                authorization = getAccessTokenHeader(it),
                                ids = ids
                        ).toObservable()
                    }
                }
                .zipWith(
                        Observable.range(0, Int.MAX_VALUE),
                        BiFunction<TracksOnlyResponse, Int, Pair<TracksOnlyResponse, Int>> { response, index -> Pair(response, index) }
                )
                .map { (response, index) ->
                    response.tracks.mapIndexed { i: Int, trackData: TrackApiModel ->
                        TopTrackEntity(index * 50 + i + 1, trackData.domain)
                    }
                }

    override val currentUser: Single<UserEntity>
        get() = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
            api.getCurrentUser(getAccessTokenHeader(token))
                    .map(UserApiModel::domain)
        }

    override fun searchAll(
            query: String,
            offset: Int
    ): Single<SearchAllEntity> = preferences.accessToken.single.loadIfNeededThenFlatMapValid { token ->
        api.searchAll(authorization = getAccessTokenHeader(token), query = query)
                .map {
                    SearchAllEntity(
                            albums = it.albumsResult?.albums?.map(AlbumApiModel::domain)
                                    ?: emptyList(),
                            artists = it.artistsResult?.artists?.map(ArtistApiModel::domain)
                                    ?: emptyList(),
                            playlists = it.playlistsResult?.playlists?.map(PlaylistApiModel::domain)
                                    ?: emptyList(),
                            tracks = it.tracksResult?.tracks?.map(TrackApiModel::domain)
                                    ?: emptyList(),
                            totalItems = arrayOf(
                                    it.albumsResult?.totalItems ?: 0,
                                    it.artistsResult?.totalItems ?: 0,
                                    it.playlistsResult?.totalItems ?: 0,
                                    it.tracksResult?.totalItems ?: 0
                            ).max() ?: 0
                    )
                }
    }

    override fun getPlaylistsForCategory(
            categoryId: String,
            offset: Int
    ): Single<EntityPage<PlaylistEntity>> = preferences.accessToken.single.loadIfNeededThenFlatMapValid { token ->
        api.getPlaylistsForCategory(
                authorization = getAccessTokenHeader(token),
                categoryId = categoryId,
                offset = offset,
                country = preferences.country
        ).map {
            EntityPage(
                    items = it.result.playlists.map(PlaylistApiModel::domain),
                    offset = it.result.offset,
                    totalItems = it.result.totalItems
            )
        }
    }

    override fun getPlaylistTracks(
            playlistId: String,
            userId: String,
            offset: Int
    ): Single<EntityPage<TrackEntity>> = preferences.accessToken.single.loadIfNeededThenFlatMapValid { token ->
        api.getPlaylistTracks(
                authorization = getAccessTokenHeader(token),
                playlistId = playlistId,
                userId = userId,
                offset = offset
        ).map { response ->
            EntityPage(
                    items = response.playlistTracks.map { it.track.domain },
                    offset = response.offset,
                    totalItems = response.totalItems
            )
        }
    }

    override fun getAlbum(
            albumId: String
    ): Single<AlbumEntity> = preferences.accessToken.single.loadIfNeededThenFlatMapValid { token ->
        api.getAlbum(
                authorization = getAccessTokenHeader(token),
                albumId = albumId
        ).map(AlbumApiModel::domain)
    }

    override fun getArtists(
            artistIds: List<String>
    ): Single<List<ArtistEntity>> = preferences.accessToken.single.loadIfNeededThenFlatMapValid { token ->
        api.getArtists(
                authorization = getAccessTokenHeader(token),
                artistIds = artistIds.joinToString(",")
        ).map { it.artists.map(ArtistApiModel::domain) }
    }

    override fun getSimilarTracks(
            trackId: String
    ): Observable<List<TrackEntity>> = preferences.accessToken.observable.loadIfNeededThenFlatMapValid { token ->
        api.getSimilarTracks(authorization = getAccessTokenHeader(token), trackId = trackId)
                .map { response ->
                    response.tracks.chunked(50)
                            .map { it.joinToString(",") { track -> track.id } }
                }
                .toObservable()
                .flatMapIterable { it }
                .flatMap {
                    api.getTracks(
                            authorization = getAccessTokenHeader(token),
                            ids = it
                    ).toObservable()
                }
                .map { it.tracks.map(TrackApiModel::domain) }
    }

    override fun getAlbumsFromArtist(
            artistId: String
    ): Observable<List<AlbumEntity>> {
        val offsetSubject = BehaviorSubject.createDefault(0)
        return offsetSubject.concatMap { offset ->
            preferences.accessToken.observable.loadIfNeededThenFlatMapValid { token ->
                api.getAlbumsFromArtist(
                        authorization = getAccessTokenHeader(token),
                        artistId = artistId,
                        offset = offset
                ).toObservable()
            }
        }.doOnNext {
            if (it.offset < it.totalItems - SpotifyApi.DEFAULT_LIMIT)
                offsetSubject.onNext(it.offset + SpotifyApi.DEFAULT_LIMIT)
            else
                offsetSubject.onComplete()
        }.map { it.albums.map(AlbumApiModel::domain) }
    }

    override fun getTopTracksFromArtist(
            artistId: String
    ): Single<List<TrackEntity>> = preferences.accessToken.single.loadIfNeededThenFlatMapValid { token ->
        api.getTopTracksFromArtist(
                authorization = getAccessTokenHeader(token),
                artistId = artistId,
                country = preferences.country
        ).map { it.tracks.map(TrackApiModel::domain) }
    }

    override fun getRelatedArtists(
            artistId: String
    ): Single<List<ArtistEntity>> = preferences.accessToken.single.loadIfNeededThenFlatMapValid { token ->
        api.getRelatedArtists(
                authorization = getAccessTokenHeader(token),
                artistId = artistId
        ).map { it.artists.map(ArtistApiModel::domain) }
    }

    class TrackIdsPage(
            val ids: String,
            val offset: Int,
            val totalItems: Int
    )

    override fun getTracksFromAlbum(
            albumId: String
    ): Observable<EntityPage<TrackEntity>> {
        val offsetSubject = BehaviorSubject.createDefault(0)
        return offsetSubject.concatMap { offset ->
            preferences.accessToken.observable.loadIfNeededThenFlatMapValid { token ->
                api.getTracksFromAlbum(
                        authorization = getAccessTokenHeader(token),
                        albumId = albumId,
                        offset = offset
                ).toObservable().map {
                    TrackIdsPage(
                            ids = it.tracks.joinToString(separator = ",") { it.id },
                            offset = it.offset,
                            totalItems = it.totalItems
                    )
                }.flatMap { idsPage ->
                    api.getTracks(
                            authorization = getAccessTokenHeader(token),
                            ids = idsPage.ids
                    ).toObservable().map {
                        EntityPage(
                                items = it.tracks.map(TrackApiModel::domain),
                                offset = idsPage.offset,
                                totalItems = idsPage.totalItems
                        )
                    }
                }
            }.doOnNext {
                if (it.offset < it.totalItems - SpotifyApi.DEFAULT_LIMIT) offsetSubject.onNext(it.offset + SpotifyApi.DEFAULT_LIMIT)
                else offsetSubject.onComplete()
            }
        }

    }

    override fun getNewReleases(
            offset: Int
    ): Single<EntityPage<AlbumEntity>> = preferences.accessToken.single.loadIfNeededThenFlatMapValid { token ->
        api.getNewReleases(
                authorization = getAccessTokenHeader(token),
                offset = offset
        ).map {
            EntityPage(
                    items = it.result.albums.map(AlbumApiModel::domain),
                    offset = it.result.offset,
                    totalItems = it.result.totalItems
            )
        }
    }

    override fun getCurrentUsersPlaylists(
            offset: Int
    ): Single<EntityPage<PlaylistEntity>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersPlaylists(
                authorization = getAccessTokenHeader(token),
                offset = offset
        ).map {
            EntityPage(
                    items = it.playlists.map(PlaylistApiModel::domain),
                    offset = it.offset,
                    totalItems = it.totalItems
            )
        }
    }

    override fun getCurrentUsersTopTracks(
            offset: Int
    ): Single<EntityPage<TrackEntity>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersTopTracks(
                authorization = getAccessTokenHeader(token),
                offset = offset
        ).map {
            EntityPage(
                    items = it.tracks.map(TrackApiModel::domain),
                    offset = it.offset,
                    totalItems = it.totalItems
            )
        }
    }

    override fun getCurrentUsersTopArtists(
            offset: Int
    ): Single<EntityPage<ArtistEntity>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersTopArtists(
                authorization = getAccessTokenHeader(token),
                offset = offset
        ).map {
            EntityPage(
                    items = it.artists.map(ArtistApiModel::domain),
                    offset = it.offset,
                    totalItems = it.totalItems
            )
        }
    }

    override fun getCurrentUsersSavedTracks(
            offset: Int
    ): Single<EntityPage<TrackEntity>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersSavedTracks(
                authorization = getAccessTokenHeader(token),
                offset = offset
        ).map { result ->
            EntityPage(
                    items = result.savedTracks.map { it.track.domain },
                    offset = result.offset,
                    totalItems = result.totalItems
            )
        }
    }

    override fun getCurrentUsersSavedAlbums(
            offset: Int
    ): Single<EntityPage<AlbumEntity>> = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
        api.getCurrentUsersSavedAlbums(
                authorization = getAccessTokenHeader(token),
                offset = offset
        ).map { result ->
            EntityPage(
                    items = result.savedAlbums.map { it.album.domain },
                    offset = result.offset,
                    totalItems = result.totalItems
            )
        }
    }

    override fun getAudioFeatures(
            trackEntity: TrackEntity
    ): Single<AudioFeaturesEntity> = preferences.accessToken.single.loadIfNeededThenFlatMapValid { token ->
        api.getAudioFeatures(
                authorization = getAccessTokenHeader(token),
                trackId = trackEntity.id
        ).map(AudioFeaturesApiModel::domain)
    }

    private fun <T> Observable<SpotifyPreferences.SavedAccessTokenEntity>.loadIfNeededThenFlatMapValid(
            block: (String) -> Observable<T>
    ): Observable<T> = flatMap { saved ->
        when (saved) {
            is SpotifyPreferences.SavedAccessTokenEntity.Valid -> block(saved.token)
            else -> accessToken.toObservable()
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
        fun getAccessTokenHeader(accessToken: String): String = "Bearer $accessToken"

        private const val ACCESS_TOKEN_UNAVAILABLE = "Access token unavailable."

        val clientDataHeader: String
            get() {
                val encoded = Base64.encodeToString("${SpotifyAuth.id}:${SpotifyAuth.secret}".toByteArray(), Base64.NO_WRAP)
                return "Basic $encoded"
            }
    }
}