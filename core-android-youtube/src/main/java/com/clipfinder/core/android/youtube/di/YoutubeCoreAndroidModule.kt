package com.clipfinder.core.android.youtube.di

import com.clipfinder.core.android.util.ext.buildRoom
import com.clipfinder.core.android.youtube.api.YoutubeApi
import com.clipfinder.core.android.youtube.db.YoutubeDb
import com.clipfinder.core.android.youtube.db.model.SearchResponseEntity
import com.clipfinder.core.android.youtube.repo.YoutubeRepo
import com.clipfinder.core.ext.qualifier
import com.clipfinder.core.youtube.api.IYoutubeApi
import com.clipfinder.core.youtube.model.SearchType
import com.clipfinder.core.youtube.repo.IYoutubeRepo
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.store.rx2.getSingle
import com.dropbox.store.rx2.ofMaybe
import com.dropbox.store.rx2.ofSingle
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Completable
import io.reactivex.Single
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import org.threeten.bp.OffsetDateTime

typealias YoutubeRelatedVideosSearchStore =
    Store<YoutubeRelatedVideosSearchStoreArgs, SearchListResponse>

typealias YoutubeQuerySearchStore = Store<YoutubeQuerySearchStoreArgs, SearchListResponse>

fun YoutubeQuerySearchStore.searchVideos(
    query: String,
    pageToken: String?
): Single<SearchListResponse> =
    getSingle(YoutubeQuerySearchStoreArgs(query, SearchType.video, pageToken))

fun YoutubeRelatedVideosSearchStore.searchRelatedVideos(
    videoId: String,
    pageToken: String?
): Single<SearchListResponse> = getSingle(YoutubeRelatedVideosSearchStoreArgs(videoId, pageToken))

fun YoutubeQuerySearchStore.searchPlaylists(
    query: String,
    pageToken: String?
): Single<SearchListResponse> =
    getSingle(YoutubeQuerySearchStoreArgs(query, SearchType.playlist, pageToken))

fun YoutubeQuerySearchStore.searchChannels(
    query: String,
    pageToken: String?
): Single<SearchListResponse> =
    getSingle(YoutubeQuerySearchStoreArgs(query, SearchType.channel, pageToken))

data class YoutubeQuerySearchStoreArgs(
    val query: String,
    val type: SearchType,
    val pageToken: String? = null
)

data class YoutubeRelatedVideosSearchStoreArgs(val query: String, val pageToken: String? = null)

enum class YoutubeSearchStoreType {
    QUERY,
    RELATED
}

val youtubeCoreAndroidModule = module {
    single<YouTube> {
        YouTube.Builder(NetHttpTransport(), GsonFactory.getDefaultInstance(), null).build()
    }
    single { YoutubeApi(get()) } bind IYoutubeApi::class
    single { androidContext().buildRoom<YoutubeDb>() }

    single(YoutubeSearchStoreType.QUERY.qualifier) {
        val dao = get<YoutubeDb>().searchDao()
        StoreBuilder.from<YoutubeQuerySearchStoreArgs, SearchListResponse, SearchListResponse>(
                fetcher =
                    Fetcher.ofSingle { (query, searchType, pageToken) ->
                        get<IYoutubeApi>().search(query, searchType, pageToken)
                    },
                sourceOfTruth =
                    SourceOfTruth.ofMaybe(
                        reader = { (query, searchType, pageToken) ->
                            dao.selectByQuerySearchTypeAndPageToken(query, searchType, pageToken)
                                .map(SearchResponseEntity::content)
                        },
                        writer = { (query, searchType, pageToken), content ->
                            Completable.fromAction {
                                dao.insert(
                                    SearchResponseEntity(
                                        query = query,
                                        pageToken = pageToken,
                                        searchType = searchType,
                                        content = content,
                                        cachedAt = OffsetDateTime.now()
                                    )
                                )
                            }
                        },
                        delete = { (query, searchType, pageToken) ->
                            Completable.fromAction {
                                dao.deleteByQuerySearchTypeAndPageToken(
                                    query = query,
                                    searchType = searchType,
                                    pageToken = pageToken
                                )
                            }
                        },
                        deleteAll = { Completable.fromAction(dao::deleteAll) }
                    )
            )
            .build()
    }

    single(YoutubeSearchStoreType.RELATED.qualifier) {
        val dao = get<YoutubeDb>().searchDao()
        StoreBuilder.from<
                YoutubeRelatedVideosSearchStoreArgs, SearchListResponse, SearchListResponse>(
                fetcher =
                    Fetcher.ofSingle { (videoId, pageToken) ->
                        get<IYoutubeApi>().searchRelatedVideos(videoId, pageToken)
                    },
                sourceOfTruth =
                    SourceOfTruth.ofMaybe(
                        reader = { (videoId, pageToken) ->
                            dao.selectByRelatedVideoIdAndPageToken(videoId, pageToken)
                                .map(SearchResponseEntity::content)
                        },
                        writer = { (relatedVideoId, pageToken), content ->
                            Completable.fromAction {
                                dao.insert(
                                    SearchResponseEntity(
                                        pageToken = pageToken,
                                        content = content,
                                        searchType = SearchType.video,
                                        cachedAt = OffsetDateTime.now(),
                                        relatedVideoId = relatedVideoId
                                    )
                                )
                            }
                        },
                        delete = { (relatedVideoId, pageToken) ->
                            Completable.fromAction {
                                dao.deleteByRelatedVideoIdAndPageToken(relatedVideoId, pageToken)
                            }
                        },
                        deleteAll = { Completable.fromAction(dao::deleteAll) }
                    )
            )
            .build()
    }

    single {
        YoutubeRepo(
            querySearchStore = get(YoutubeSearchStoreType.QUERY.qualifier),
            relatedVideosSearchStore = get(YoutubeSearchStoreType.RELATED.qualifier),
            searchDao = get<YoutubeDb>().searchDao()
        )
    } bind IYoutubeRepo::class
}
