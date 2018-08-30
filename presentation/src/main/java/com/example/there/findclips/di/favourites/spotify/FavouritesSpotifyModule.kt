package com.example.there.findclips.di.favourites.spotify

import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.spotify.*
import com.example.there.findclips.fragments.favourites.spotify.SpotifyFavouritesVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@Module
class FavouritesSpotifyModule {

    @Provides
    fun getFavouriteAlbumsUseCase(repository: ISpotifyRepository): GetFavouriteAlbums = GetFavouriteAlbums(AsyncTransformer(), repository)

    @Provides
    fun getFavouriteArtistsUseCase(repository: ISpotifyRepository): GetFavouriteArtists = GetFavouriteArtists(AsyncTransformer(), repository)

    @Provides
    fun getFavouriteCategoriesUseCase(repository: ISpotifyRepository): GetFavouriteCategories = GetFavouriteCategories(AsyncTransformer(), repository)

    @Provides
    fun getFavouriteSpotifyPlaylistsUseCase(repository: ISpotifyRepository): GetFavouriteSpotifyPlaylists =
            GetFavouriteSpotifyPlaylists(AsyncTransformer(), repository)

    @Provides
    fun getFavouriteTracksUseCase(repository: ISpotifyRepository): GetFavouriteTracks = GetFavouriteTracks(AsyncTransformer(), repository)

    @Provides
    fun favouritesViewModelFactory(getFavouriteAlbums: GetFavouriteAlbums,
                                   getFavouriteArtists: GetFavouriteArtists,
                                   getFavouriteCategories: GetFavouriteCategories,
                                   getFavouriteSpotifyPlaylists: GetFavouriteSpotifyPlaylists,
                                   getFavouriteTracks: GetFavouriteTracks): SpotifyFavouritesVMFactory = SpotifyFavouritesVMFactory(
            getFavouriteAlbums,
            getFavouriteArtists,
            getFavouriteCategories,
            getFavouriteSpotifyPlaylists,
            getFavouriteTracks
    )
}