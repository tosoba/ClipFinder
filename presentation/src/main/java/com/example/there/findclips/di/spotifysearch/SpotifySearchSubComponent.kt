package com.example.there.findclips.di.spotifysearch

import com.example.there.findclips.search.spotify.SpotifySearchFragment
import dagger.Subcomponent

@SpotifySearchScope
@Subcomponent(modules = [
    SpotifySearchModule::class
])
interface SpotifySearchSubComponent {
    fun inject(fragment: SpotifySearchFragment)
}