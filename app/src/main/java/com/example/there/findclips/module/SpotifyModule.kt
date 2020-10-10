package com.example.there.findclips.module

import com.example.there.domain.usecase.spotify.GetArtists
import com.example.there.domain.usecase.spotify.GetAudioFeatures
import com.example.there.domain.usecase.spotify.GetCurrentUser
import com.example.there.domain.usecase.spotify.GetSimilarTracks
import org.koin.dsl.module

val spotifyModule = module {
    factory { GetArtists(get(), get()) }
    factory { GetAudioFeatures(get(), get()) }
    factory { GetCurrentUser(get(), get()) }
    factory { GetSimilarTracks(get(), get()) }
}
