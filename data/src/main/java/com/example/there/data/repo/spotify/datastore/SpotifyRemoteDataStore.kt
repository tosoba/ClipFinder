package com.example.there.data.repo.spotify.datastore

import android.util.Base64
import com.example.there.data.api.spotify.SpotifyAccountsApi
import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.data.api.spotify.SpotifyChartsApi
import com.example.there.data.entity.spotify.TrackData
import com.example.there.data.mapper.spotify.*
import com.example.there.data.preferences.PreferencesHelper
import com.example.there.data.response.TracksOnlyResponse
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.*
import com.example.there.domain.repo.spotify.datastore.ISpotifyRemoteDataStore
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyRemoteDataStore @Inject constructor(
        private val api: SpotifyApi,
        private val accountsApi: SpotifyAccountsApi,
        private val chartsApi: SpotifyChartsApi,
        private val preferencesHelper: PreferencesHelper
) : ISpotifyRemoteDataStore {

    override fun getAccessToken(
            clientId: String,
            clientSecret: String
    ): Single<AccessTokenEntity> = accountsApi.getAccessToken(authorization = getClientDataHeader(clientId, clientSecret))
            .map(AccessTokenMapper::mapFrom)

    override fun getCategories(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>> {
        val offsetSubject = BehaviorSubject.createDefault(0)
        return offsetSubject.concatMap { offset ->
            api.getCategories(
                    authorization = getAccessTokenHeader(accessToken.token),
                    offset = offset,
                    country = preferencesHelper.country,
                    locale = preferencesHelper.language
            )
        }.doOnNext {
            if (it.offset < it.totalItems - SpotifyApi.DEFAULT_LIMIT)
                offsetSubject.onNext(it.offset + SpotifyApi.DEFAULT_LIMIT)
            else
                offsetSubject.onComplete()
        }.map { it.result.categories.map(CategoryMapper::mapFrom) }
    }

    override fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>> {
        val offsetSubject = BehaviorSubject.createDefault(0)
        return offsetSubject.concatMap { offset ->
            api.getFeaturedPlaylists(
                    authorization = getAccessTokenHeader(accessToken.token),
                    offset = offset,
                    country = preferencesHelper.country,
                    locale = preferencesHelper.language
            )
        }.doOnNext {
            if (it.result.offset < it.result.totalItems - SpotifyApi.DEFAULT_LIMIT)
                offsetSubject.onNext(it.result.offset + SpotifyApi.DEFAULT_LIMIT)
            else
                offsetSubject.onComplete()
        }.map { it.result.playlists.map(PlaylistMapper::mapFrom) }
    }

    override fun getDailyViralTracks(
            accessToken: AccessTokenEntity
    ): Observable<List<TopTrackEntity>> = chartsApi.getDailyViralTracks()
            .map { it.split('\n').filter { it.isNotBlank() && it.first().isDigit() } }
            .map { it.map(ChartTrackIdMapper::mapFrom) }
            .map { it.chunked(50).map { it.joinToString(",") } }
            .concatMapIterable { it }
            .concatMap {
                api.getTracks(
                        authorization = getAccessTokenHeader(accessToken.token),
                        ids = it
                ).toObservable()
            }
            .zipWith(
                    Observable.range(0, Int.MAX_VALUE),
                    BiFunction<TracksOnlyResponse, Int, Pair<TracksOnlyResponse, Int>> { response, index -> Pair(response, index) }
            )
            .map { (response, index) ->
                response.tracks.mapIndexed { i: Int, trackData: TrackData ->
                    TopTrackEntity(index * 50 + i + 1, TrackMapper.mapFrom(trackData))
                }
            }


    override fun searchAll(
            accessToken: AccessTokenEntity,
            query: String,
            offset: Int
    ): Single<SearchAllEntity> = api.searchAll(authorization = getAccessTokenHeader(accessToken.token), query = query)
            .map {
                SearchAllEntity(
                        albums = it.albumsResult?.albums?.map(AlbumMapper::mapFrom)
                                ?: emptyList(),
                        artists = it.artistsResult?.artists?.map(ArtistMapper::mapFrom)
                                ?: emptyList(),
                        playlists = it.playlistsResult?.playlists?.map(PlaylistMapper::mapFrom)
                                ?: emptyList(),
                        tracks = it.tracksResult?.tracks?.map(TrackMapper::mapFrom)
                                ?: emptyList(),
                        totalItems = arrayOf(
                                it.albumsResult?.totalItems ?: 0,
                                it.artistsResult?.totalItems ?: 0,
                                it.playlistsResult?.totalItems ?: 0,
                                it.tracksResult?.totalItems ?: 0
                        ).max() ?: 0
                )
            }

    override fun getPlaylistsForCategory(
            accessToken: AccessTokenEntity,
            categoryId: String,
            offset: Int
    ): Single<EntityPage<PlaylistEntity>> = api.getPlaylistsForCategory(
            authorization = getAccessTokenHeader(accessToken.token),
            categoryId = categoryId,
            offset = offset,
            country = preferencesHelper.country
    ).map {
        EntityPage(
                items = it.result.playlists.map(PlaylistMapper::mapFrom),
                offset = it.result.offset,
                totalItems = it.result.totalItems
        )
    }

    override fun getPlaylistTracks(
            accessToken: AccessTokenEntity,
            playlistId: String,
            userId: String,
            offset: Int
    ): Single<EntityPage<TrackEntity>> = api.getPlaylistTracks(
            authorization = getAccessTokenHeader(accessToken.token),
            playlistId = playlistId,
            userId = userId,
            offset = offset
    ).map {
        EntityPage(
                items = it.playlistTracks.map { TrackMapper.mapFrom(it.track) },
                offset = it.offset,
                totalItems = it.totalItems
        )
    }

    override fun getAlbum(
            accessToken: AccessTokenEntity,
            albumId: String
    ): Single<AlbumEntity> = api.getAlbum(
            authorization = getAccessTokenHeader(accessToken.token),
            albumId = albumId
    ).map(AlbumMapper::mapFrom)

    override fun getArtists(
            accessToken: AccessTokenEntity,
            artistIds: List<String>
    ): Single<List<ArtistEntity>> = api.getArtists(
            authorization = getAccessTokenHeader(accessToken.token),
            artistIds = artistIds.joinToString(",")
    ).map { it.artists.map(ArtistMapper::mapFrom) }

    override fun getSimilarTracks(
            accessToken: AccessTokenEntity,
            trackId: String
    ): Observable<List<TrackEntity>> = api.getSimilarTracks(authorization = getAccessTokenHeader(accessToken.token), trackId = trackId)
            .map { it.tracks.chunked(50).map { it.joinToString(",") { it.id } } }
            .flatMapIterable { it }
            .flatMap {
                api.getTracks(
                        authorization = getAccessTokenHeader(accessToken.token),
                        ids = it
                ).toObservable()
            }
            .map { it.tracks.map(TrackMapper::mapFrom) }


    override fun getAlbumsFromArtist(
            accessToken: AccessTokenEntity,
            artistId: String
    ): Observable<List<AlbumEntity>> {
        val offsetSubject = BehaviorSubject.createDefault(0)
        return offsetSubject.concatMap { offset ->
            api.getAlbumsFromArtist(
                    authorization = getAccessTokenHeader(accessToken.token),
                    artistId = artistId,
                    offset = offset
            )
        }.doOnNext {
            if (it.offset < it.totalItems - SpotifyApi.DEFAULT_LIMIT)
                offsetSubject.onNext(it.offset + SpotifyApi.DEFAULT_LIMIT)
            else
                offsetSubject.onComplete()
        }.map { it.albums.map(AlbumMapper::mapFrom) }
    }

    override fun getTopTracksFromArtist(
            accessToken: AccessTokenEntity,
            artistId: String
    ): Single<List<TrackEntity>> = api.getTopTracksFromArtist(
            authorization = getAccessTokenHeader(accessToken.token),
            artistId = artistId,
            country = preferencesHelper.country
    ).map { it.tracks.map(TrackMapper::mapFrom) }

    override fun getRelatedArtists(
            accessToken: AccessTokenEntity,
            artistId: String
    ): Single<List<ArtistEntity>> = api.getRelatedArtists(
            authorization = getAccessTokenHeader(accessToken.token),
            artistId = artistId
    ).map { it.artists.map(ArtistMapper::mapFrom) }

    class TrackIdsPage(
            val ids: String,
            val offset: Int,
            val totalItems: Int
    )

    override fun getTracksFromAlbum(
            accessToken: AccessTokenEntity,
            albumId: String
    ): Observable<EntityPage<TrackEntity>> {
        val offsetSubject = BehaviorSubject.createDefault(0)
        return offsetSubject.concatMap { offset ->
            api.getTracksFromAlbum(
                    authorization = getAccessTokenHeader(accessToken.token),
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
                        authorization = getAccessTokenHeader(accessToken.token),
                        ids = idsPage.ids
                ).toObservable().map {
                    EntityPage(
                            items = it.tracks.map(TrackMapper::mapFrom),
                            offset = idsPage.offset,
                            totalItems = idsPage.totalItems
                    )
                }
            }.doOnNext {
                if (it.offset < it.totalItems - SpotifyApi.DEFAULT_LIMIT) offsetSubject.onNext(it.offset + SpotifyApi.DEFAULT_LIMIT)
                else offsetSubject.onComplete()
            }
        }

    }

    override fun getNewReleases(
            accessToken: AccessTokenEntity,
            offset: Int
    ): Single<EntityPage<AlbumEntity>> = api.getNewReleases(
            authorization = getAccessTokenHeader(accessToken.token),
            offset = offset
    ).map {
        EntityPage(
                items = it.result.albums.map(AlbumMapper::mapFrom),
                offset = it.result.offset,
                totalItems = it.result.totalItems
        )
    }

    override fun getCurrentUsersPlaylists(
            accessToken: AccessTokenEntity,
            offset: Int
    ): Single<EntityPage<PlaylistEntity>> = api.getCurrentUsersPlaylists(
            authorization = getAccessTokenHeader(accessToken.token),
            offset = offset
    ).map {
        EntityPage(
                items = it.playlists.map(PlaylistMapper::mapFrom),
                offset = it.offset,
                totalItems = it.totalItems
        )
    }

    override fun getCurrentUsersTopTracks(
            accessToken: AccessTokenEntity,
            offset: Int
    ): Single<EntityPage<TrackEntity>> = api.getCurrentUsersTopTracks(
            authorization = getAccessTokenHeader(accessToken.token),
            offset = offset
    ).map {
        EntityPage(
                items = it.tracks.map(TrackMapper::mapFrom),
                offset = it.offset,
                totalItems = it.totalItems
        )
    }

    override fun getCurrentUsersTopArtists(
            accessToken: AccessTokenEntity,
            offset: Int
    ): Single<EntityPage<ArtistEntity>> = api.getCurrentUsersTopArtists(
            authorization = getAccessTokenHeader(accessToken.token),
            offset = offset
    ).map {
        EntityPage(
                items = it.artists.map(ArtistMapper::mapFrom),
                offset = it.offset,
                totalItems = it.totalItems
        )
    }

    override fun getCurrentUsersSavedTracks(
            accessToken: AccessTokenEntity,
            offset: Int
    ): Single<EntityPage<TrackEntity>> = api.getCurrentUsersSavedTracks(
            authorization = getAccessTokenHeader(accessToken.token),
            offset = offset
    ).map { result ->
        EntityPage(
                items = result.savedTracks.map { TrackMapper.mapFrom(it.track) },
                offset = result.offset,
                totalItems = result.totalItems
        )
    }

    override fun getCurrentUsersSavedAlbums(
            accessToken: AccessTokenEntity,
            offset: Int
    ): Single<EntityPage<AlbumEntity>> = api.getCurrentUsersSavedAlbums(
            authorization = getAccessTokenHeader(accessToken.token),
            offset = offset
    ).map { result ->
        EntityPage(
                items = result.savedAlbums.map { AlbumMapper.mapFrom(it.album) },
                offset = result.offset,
                totalItems = result.totalItems
        )
    }

    override fun getCurrentUser(
            accessToken: AccessTokenEntity
    ): Single<UserEntity> = api.getCurrentUser(getAccessTokenHeader(accessToken.token))
            .map { UserMapper.mapFrom(it) }

    override fun getAudioFeatures(
            accessToken: AccessTokenEntity,
            trackEntity: TrackEntity
    ): Single<AudioFeaturesEntity> = api.getAudioFeatures(
            authorization = getAccessTokenHeader(accessToken.token),
            trackId = trackEntity.id
    ).map(AudioFeaturesMapper::mapFrom)

    companion object {
        fun getAccessTokenHeader(accessToken: String): String = "Bearer $accessToken"

        fun getClientDataHeader(clientId: String, clientSecret: String): String {
            val encoded = Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)
            return "Basic $encoded"
        }
    }
}