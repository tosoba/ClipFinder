package com.example.there.findclips.di.favourites.videos

import com.example.there.findclips.fragments.favourites.videos.VideosFavouritesFragment
import dagger.Subcomponent

@FavouritesVideosScope
@Subcomponent(modules = [FavouritesVideosModule::class])
interface FavouritesVideosSubComponent {
    fun inject(fragment: VideosFavouritesFragment)
}