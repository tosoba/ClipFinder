package com.example.there.findclips.module

import com.example.there.domain.usecase.soundcloud.*
import org.koin.dsl.module

val soundCloudModule = module {
    factory { GetSimilarTracks(get(), get()) }
    factory { GetTracks(get(), get()) }
    factory { GetTracksFromPlaylist(get(), get()) }
}