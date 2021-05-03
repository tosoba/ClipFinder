package com.clipfinder.core.android.youtube.di

import com.clipfinder.core.android.util.ext.buildRoom
import com.clipfinder.core.android.youtube.api.YoutubeApi
import com.clipfinder.core.android.youtube.db.YoutubeDb
import com.clipfinder.core.android.youtube.db.model.SearchResponseEntity
import com.clipfinder.core.android.youtube.repo.YoutubeRepo
import com.clipfinder.core.youtube.api.IYoutubeApi
import com.clipfinder.core.youtube.repo.IYoutubeRepo
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.store.rx2.ofMaybe
import com.dropbox.store.rx2.ofSingle
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Completable
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.threeten.bp.OffsetDateTime

typealias YoutubeSearchStore = Store<Pair<String, String?>, SearchListResponse>

enum class YoutubeSearchStoreType {
    QUERY,
    RELATED;

    val qualifier: Qualifier
        get() = named(name)
}

val youtubeCoreAndroidModule = module {
    single<YouTube> {
        YouTube.Builder(NetHttpTransport(), GsonFactory.getDefaultInstance(), null).build()
    }
    single { YoutubeApi(get()) } bind IYoutubeApi::class
    single { androidContext().buildRoom<YoutubeDb>() }

    single(YoutubeSearchStoreType.QUERY.qualifier) {
        val dao = get<YoutubeDb>().searchDao()
        StoreBuilder.from(
                fetcher =
                    Fetcher.ofSingle { key: Pair<String, String?> ->
                        val (query, pageToken) = key
                        get<IYoutubeApi>().search(query, pageToken)
                    },
                sourceOfTruth =
                    SourceOfTruth.ofMaybe<
                        Pair<String, String?>, SearchListResponse, SearchListResponse>(
                        reader = { (query, pageToken) ->
                            dao.selectByQueryAndPageToken(query, pageToken)
                                .map(SearchResponseEntity::content)
                        },
                        writer = { (query, pageToken), content ->
                            Completable.fromAction {
                                dao.insert(
                                    SearchResponseEntity(
                                        query = query,
                                        pageToken = pageToken,
                                        content = content,
                                        cachedAt = OffsetDateTime.now()
                                    )
                                )
                            }
                        },
                        delete = { (query, pageToken) ->
                            Completable.fromAction {
                                dao.deleteByQueryAndPageToken(query, pageToken)
                            }
                        },
                        deleteAll = { Completable.fromAction(dao::deleteAll) }
                    )
            )
            .build()
    }

    single(YoutubeSearchStoreType.RELATED.qualifier) {
        val dao = get<YoutubeDb>().searchDao()
        StoreBuilder.from(
                fetcher =
                    Fetcher.ofSingle { key: Pair<String, String?> ->
                        val (videoId, pageToken) = key
                        get<IYoutubeApi>().searchRelatedVideos(videoId, pageToken)
                    },
                sourceOfTruth =
                    SourceOfTruth.ofMaybe<
                        Pair<String, String?>, SearchListResponse, SearchListResponse>(
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
            relatedSearchStore = get(YoutubeSearchStoreType.RELATED.qualifier),
            searchDao = get<YoutubeDb>().searchDao()
        )
    } bind IYoutubeRepo::class
}
