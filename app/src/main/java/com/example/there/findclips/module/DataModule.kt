package com.example.there.findclips.module

import androidx.room.Room
import com.example.db.FindClipsDb
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), FindClipsDb::class.java, "FindClipsDb.db").build()
    }
    factory { get<FindClipsDb>().albumDao() }
    factory { get<FindClipsDb>().artistDao() }
    factory { get<FindClipsDb>().spotifyPlaylistDao() }
    factory { get<FindClipsDb>().trackDao() }
    factory { get<FindClipsDb>().videoDao() }
    factory { get<FindClipsDb>().videoPlaylistDao() }
    factory { get<FindClipsDb>().videoSearchDao() }
    factory { get<FindClipsDb>().relatedVideoSearchDao() }
}

