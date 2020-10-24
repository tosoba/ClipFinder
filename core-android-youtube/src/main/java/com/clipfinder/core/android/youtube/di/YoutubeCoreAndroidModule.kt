package com.clipfinder.core.android.youtube.di

import com.clipfinder.core.android.youtube.R
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
import com.example.core.android.util.ext.buildRoom
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchListResponse
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import org.threeten.bp.OffsetDateTime

typealias YoutubeSearchStore = Store<Pair<String, String?>, SearchListResponse>

val youtubeCoreAndroidModule = module {
    single<YouTube> { YouTube.Builder(NetHttpTransport(), GsonFactory(), null).build() }
    single { YoutubeApi(androidContext().getString(R.string.youtube_api_key), get()) } bind IYoutubeApi::class
    single { androidContext().buildRoom<YoutubeDb>() }
    single<YoutubeSearchStore> {
        val dao = get<YoutubeDb>().searchDao()
        StoreBuilder
            .from(
                fetcher = Fetcher.ofSingle { key: Pair<String, String?> ->
                    val (query, pageToken) = key
                    get<IYoutubeApi>().search(query, pageToken)
                },
                sourceOfTruth = SourceOfTruth.ofMaybe(
                    reader = { (query, pageToken) ->
                        dao.select(query, pageToken).map(SearchResponseEntity::content)
                    },
                    writer = { (query, pageToken), content ->
                        dao.insert(SearchResponseEntity(query, pageToken, content, OffsetDateTime.now()))
                    },
                    delete = { (query, pageToken) -> dao.delete(query, pageToken) },
                    deleteAll = dao::deleteAll
                )
            )
            .build()
    }
    single { YoutubeRepo(get(), get()) } bind IYoutubeRepo::class
}
