package com.clipfinder.core.android.youtube.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.clipfinder.core.android.youtube.R
import com.clipfinder.core.android.youtube.api.YoutubeApi
import com.clipfinder.core.android.youtube.db.YoutubeDb
import com.clipfinder.core.android.youtube.db.model.SearchResponseEntity
import com.clipfinder.core.youtube.api.IYoutubeApi
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.store.rx2.ofMaybe
import com.dropbox.store.rx2.ofSingle
import com.google.api.services.youtube.model.SearchListResponse
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import org.threeten.bp.OffsetDateTime

typealias YoutubeSearchStore = Store<Pair<String, String?>, SearchListResponse>

inline fun <reified T : RoomDatabase> Context.buildRoom(
    inMemory: Boolean = false,
    name: String = T::class.java.simpleName,
    noinline configure: (RoomDatabase.Builder<T>.() -> RoomDatabase.Builder<T>)? = null
): T {
    val builder = if (inMemory) {
        Room.inMemoryDatabaseBuilder(this, T::class.java)
    } else {
        Room.databaseBuilder(this, T::class.java, name)
    }
    if (configure != null) builder.configure()
    return builder.build()
}

val youtubeCoreAndroidModule = module {
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
}
