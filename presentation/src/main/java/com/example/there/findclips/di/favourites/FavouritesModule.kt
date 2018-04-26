package com.example.there.findclips.di.favourites

import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.findclips.favourites.FavouritesViewModelFactory
import dagger.Module
import dagger.Provides

@FavouritesScope
@Module
class FavouritesModule {

    @Provides
    fun favouritesViewModelFactory(accessTokenUseCase: AccessTokenUseCase): FavouritesViewModelFactory = FavouritesViewModelFactory(accessTokenUseCase)
}