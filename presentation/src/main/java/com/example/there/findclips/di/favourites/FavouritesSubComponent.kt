package com.example.there.findclips.di.favourites

import com.example.there.findclips.fragments.favourites.FavouritesFragment
import dagger.Subcomponent

@FavouritesScope
@Subcomponent(modules = [FavouritesModule::class])
interface FavouritesSubComponent {
    fun inject(fragment: FavouritesFragment)
}