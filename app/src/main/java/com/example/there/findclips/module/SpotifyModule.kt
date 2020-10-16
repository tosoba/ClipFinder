package com.example.there.findclips.module

import com.clipfinder.core.spotify.usecase.GetAudioFeatures
import com.example.there.domain.usecase.spotify.GetCurrentUser
import org.koin.dsl.module

val spotifyModule = module {
    factory { GetAudioFeatures(get(), get()) }
    factory { GetCurrentUser(get(), get()) }
}
