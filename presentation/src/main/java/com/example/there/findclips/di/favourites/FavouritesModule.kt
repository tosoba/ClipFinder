package com.example.there.findclips.di.favourites

import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.usecases.spotify.*
import com.example.there.domain.usecases.videos.GetFavouriteVideoPlaylists
import com.example.there.domain.usecases.videos.GetFavouriteVideosFromPlaylist
import com.example.there.findclips.fragments.favourites.FavouritesVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@FavouritesScope
@Module
class FavouritesModule {

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
    fun getVideosFromPlaylistsUseCase(repository: IVideosRepository): GetFavouriteVideosFromPlaylist =
            GetFavouriteVideosFromPlaylist(AsyncTransformer(), repository)

    @Provides
    fun favouritesViewModelFactory(getFavouriteAlbums: GetFavouriteAlbums,
                                   getFavouriteArtists: GetFavouriteArtists,
                                   getFavouriteCategories: GetFavouriteCategories,
                                   getFavouriteSpotifyPlaylists: GetFavouriteSpotifyPlaylists,
                                   getFavouriteTracks: GetFavouriteTracks,
                                   getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists,
                                   getFavouriteVideosFromPlaylist: GetFavouriteVideosFromPlaylist): FavouritesVMFactory = FavouritesVMFactory(
            getFavouriteAlbums,
            getFavouriteArtists,
            getFavouriteCategories,
            getFavouriteSpotifyPlaylists,
            getFavouriteTracks,
            getFavouriteVideoPlaylists,
            getFavouriteVideosFromPlaylist
    )
}