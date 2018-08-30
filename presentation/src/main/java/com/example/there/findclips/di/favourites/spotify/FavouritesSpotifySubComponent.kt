package com.example.there.findclips.di.favourites.spotify

import com.example.there.findclips.fragments.favourites.spotify.SpotifyFavouritesFragment
import dagger.Subcomponent

@FavouritesSpotifyScope
@Subcomponent(modules = [FavouritesSpotifyModule::class])
interface FavouritesSpotifySubComponent {
    fun inject(fragment: SpotifyFavouritesFragment)
}