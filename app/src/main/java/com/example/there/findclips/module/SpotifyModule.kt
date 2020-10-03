package com.example.there.findclips.module

import com.example.there.domain.usecase.spotify.*
import org.koin.dsl.module

val spotifyModule = module {
    factory { GetAlbum(get(), get()) }
    factory { GetArtists(get(), get()) }
    factory { GetAudioFeatures(get(), get()) }
    factory { GetCurrentUser(get(), get()) }
    factory { GetSimilarTracks(get(), get()) }
}
