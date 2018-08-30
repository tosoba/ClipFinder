package com.example.there.findclips.di.favourites.videos

import com.example.there.domain.usecases.videos.GetFavouriteVideoPlaylists
import com.example.there.findclips.fragments.favourites.videos.VideosFavouritesVMFactory
import dagger.Module
import dagger.Provides

@Module
class FavouritesVideosModule {

    @Provides
    fun videosFavouritesVMFactory(getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists) = VideosFavouritesVMFactory(getFavouriteVideoPlaylists)
}