package com.example.there.findclips.di.favourites

import com.example.there.findclips.favourites.FavouritesFragment
import dagger.Subcomponent

@FavouritesScope
@Subcomponent(modules = [FavouritesModule::class])
interface FavouritesSubComponent {
    fun inject(fragment: FavouritesFragment)
}