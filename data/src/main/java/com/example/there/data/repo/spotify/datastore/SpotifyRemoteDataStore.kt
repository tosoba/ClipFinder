package com.example.there.data.repo.spotify.datastore

import android.util.Base64
import com.example.there.data.api.spotify.SpotifyAccountsApi
import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.data.api.spotify.SpotifyChartsApi
import com.example.there.data.entity.spotify.TrackData
import com.example.there.data.mapper.spotify.*
import com.example.there.data.response.TracksOnlyResponse
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.*
import com.example.there.domain.repo.spotify.datastore.ISpotifyRemoteDataStore
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class SpotifyRemoteDataStore @Inject constructor(
        private val api: SpotifyApi,
        private val accountsApi: SpotifyAccountsApi,
        private val chartsApi: SpotifyChartsApi
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
                    offset = offset.toString()
            )
        }.doOnNext {
            if (it.offset < it.totalItems - SpotifyApi.DEFAULT_LIMIT.toInt())
                offsetSubject.onNext(it.offset + SpotifyApi.DEFAULT_LIMIT.toInt())
            else
                offsetSubject.onComplete()
        }.map { it.result.categories.map(CategoryMapper::mapFrom) }
    }

    override fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>> {
        val offsetSubject = BehaviorSubject.createDefault(0)
        return offsetSubject.concatMap { offset ->
            api.getFeaturedPlaylists(
                    authorization = getAccessTokenHeader(accessToken.token),
                    offset = offset.toString()
            )
        }.doOnNext {
            if (it.result.offset < it.result.totalItems - SpotifyApi.DEFAULT_LIMIT.toInt())
                offsetSubject.onNext(it.result.offset + SpotifyApi.DEFAULT_LIMIT.toInt())
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


    override fun searchAll(accessToken: AccessTokenEntity, query: String): Observable<SearchAllEntity> =
            api.searchAll(authorization = getAccessTokenHeader(accessToken.token), query = query)
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
                                albumsTotalResults = it.albumsResult?.totalItems ?: 0,
                                albumsOffset = it.albumsResult?.offset ?: 0,
                                artistsTotalResults = it.artistsResult?.totalItems ?: 0,
                                artistsOffset = it.artistsResult?.offset ?: 0,
                                playlistsTotalResults = it.playlistsResult?.totalItems ?: 0,
                                playlistsOffset = it.playlistsResult?.offset ?: 0,
                                tracksTotalResults = it.tracksResult?.totalItems ?: 0,
                                tracksOffset = it.tracksResult?.offset ?: 0
                        )
                    }

    override fun getPlaylistsForCategory(
            accessToken: AccessTokenEntity,
            categoryId: String,
            offset: Int
    ): Single<EntityPage<PlaylistEntity>> = api.getPlaylistsForCategory(
            authorization = getAccessTokenHeader(accessToken.token),
            categoryId = categoryId,
            offset = offset.toString()
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
            offset = offset.toString()
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
                    offset = offset.toString()
            )
        }.doOnNext {
            if (it.offset < it.totalItems - SpotifyApi.DEFAULT_LIMIT.toInt())
                offsetSubject.onNext(it.offset + SpotifyApi.DEFAULT_LIMIT.toInt())
            else
                offsetSubject.onComplete()
        }.map { it.albums.map(AlbumMapper::mapFrom) }
    }

    override fun getTopTracksFromArtist(
            accessToken: AccessTokenEntity,
            artistId: String
    ): Single<List<TrackEntity>> = api.getTopTracksFromArtist(
            authorization = getAccessTokenHeader(accessToken.token),
            artistId = artistId
    ).map { it.tracks.map(TrackMapper::mapFrom) }

    override fun getRelatedArtists(
            accessToken: AccessTokenEntity,
            artistId: String
    ): Single<List<ArtistEntity>> = api.getRelatedArtists(
            authorization = getAccessTokenHeader(accessToken.token),
            artistId = artistId
    ).map { it.artists.map(ArtistMapper::mapFrom) }

    data class TrackIdsPage(
            val ids: String,
            val offset: Int,
            val totalItems: Int
    )

    override fun getTracksFromAlbum(
            accessToken: AccessTokenEntity,
            albumId: String,
            offset: Int
    ): Single<EntityPage<TrackEntity>> = api.getTracksFromAlbum(
            authorization = getAccessTokenHeader(accessToken.token),
            albumId = albumId,
            offset = offset.toString()
    ).map {
        TrackIdsPage(
                ids = it.tracks.joinToString(separator = ",") { it.id },
                offset = it.offset,
                totalItems = it.totalItems
        )
    }.flatMap { idsPage ->
        api.getTracks(
                authorization = getAccessTokenHeader(accessToken.token),
                ids = idsPage.ids
        ).map {
            EntityPage(
                    items = it.tracks.map(TrackMapper::mapFrom),
                    offset = idsPage.offset,
                    totalItems = idsPage.totalItems
            )
        }
    }

    override fun getNewReleases(
            accessToken: AccessTokenEntity,
            offset: Int
    ): Single<EntityPage<AlbumEntity>> = api.getNewReleases(
            authorization = getAccessTokenHeader(accessToken.token),
            offset = offset.toString()
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
            offset = offset.toString()
    ).map {
        EntityPage(
                items = it.playlists.map(PlaylistMapper::mapFrom),
                offset = it.offset,
                totalItems = it.totalItems
        )
    }

    companion object {
        fun getAccessTokenHeader(accessToken: String): String = "Bearer $accessToken"

        fun getClientDataHeader(clientId: String, clientSecret: String): String {
            val encoded = Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)
            return "Basic $encoded"
        }
    }
}