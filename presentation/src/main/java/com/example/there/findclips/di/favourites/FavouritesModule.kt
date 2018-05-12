package com.example.there.findclips.di.favourites

import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.findclips.favourites.FavouritesVMFactory
import dagger.Module
import dagger.Provides

@FavouritesScope
@Module
class FavouritesModule {

    @Provides
    fun favouritesViewModelFactory(getAccessToken: GetAccessToken): FavouritesVMFactory = FavouritesVMFactory(getAccessToken)
}