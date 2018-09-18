package com.example.there.findclips.di.module.domain

import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.spotify.*
import com.example.there.findclips.util.rx.AsyncCompletableTransformer
import com.example.there.findclips.util.rx.AsyncSymmetricFlowableTransformer
import com.example.there.findclips.util.rx.AsyncSymmetricObservableTransformer
import com.example.there.findclips.util.rx.AsyncSymmetricSingleTransformer
import dagger.Module
import dagger.Provides

@Module
class SpotifyDomainModule {

    @Provides
    fun accessTokenUseCase(
            repository: ISpotifyRepository
    ): GetAccessToken = GetAccessToken(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun artistsUseCase(
            repository: ISpotifyRepository
    ): GetArtists = GetArtists(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun insertAlbumUseCase(
            repository: ISpotifyRepository
    ): InsertAlbum = InsertAlbum(AsyncCompletableTransformer(), repository)

    @Provides
    fun tracksFromAlbumUseCase(
            repository: ISpotifyRepository
    ): GetTracksFromAlbum = GetTracksFromAlbum(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun insertArtistUseCase(
            repository: ISpotifyRepository
    ): InsertArtist = InsertArtist(AsyncCompletableTransformer(), repository)

    @Provides
    fun albumsFromArtistUseCase(
            repository: ISpotifyRepository
    ): GetAlbumsFromArtist = GetAlbumsFromArtist(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun topTracksFromArtistUseCase(
            repository: ISpotifyRepository
    ): GetTopTracksFromArtist = GetTopTracksFromArtist(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun relatedArtistsUseCase(
            repository: ISpotifyRepository
    ): GetRelatedArtists = GetRelatedArtists(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun insertCategoryUseCase(
            repository: ISpotifyRepository
    ): InsertCategory = InsertCategory(AsyncCompletableTransformer(), repository)

    @Provides
    fun playlistsForCategoryUseCase(
            repository: ISpotifyRepository
    ): GetPlaylistsForCategory = GetPlaylistsForCategory(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun categoriesUseCase(
            repository: ISpotifyRepository
    ): GetCategories = GetCategories(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun featuredPlaylistsUseCase(
            repository: ISpotifyRepository
    ): GetFeaturedPlaylists = GetFeaturedPlaylists(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun dailyViralTracksUseCase(
            repository: ISpotifyRepository
    ): GetDailyViralTracks = GetDailyViralTracks(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun getFavouriteAlbumsUseCase(
            repository: ISpotifyRepository
    ): GetFavouriteAlbums = GetFavouriteAlbums(AsyncSymmetricFlowableTransformer(), repository)

    @Provides
    fun getFavouriteArtistsUseCase(
            repository: ISpotifyRepository
    ): GetFavouriteArtists = GetFavouriteArtists(AsyncSymmetricFlowableTransformer(), repository)

    @Provides
    fun getFavouriteCategoriesUseCase(
            repository: ISpotifyRepository
    ): GetFavouriteCategories = GetFavouriteCategories(AsyncSymmetricFlowableTransformer(), repository)

    @Provides
    fun getFavouriteSpotifyPlaylistsUseCase(
            repository: ISpotifyRepository
    ): GetFavouriteSpotifyPlaylists = GetFavouriteSpotifyPlaylists(AsyncSymmetricFlowableTransformer(), repository)

    @Provides
    fun getFavouriteTracksUseCase(
            repository: ISpotifyRepository
    ): GetFavouriteTracks = GetFavouriteTracks(AsyncSymmetricFlowableTransformer(), repository)

    @Provides
    fun insertSpotifyPlaylistUseCase(
            repository: ISpotifyRepository
    ): InsertSpotifyPlaylist = InsertSpotifyPlaylist(AsyncCompletableTransformer(), repository)

    @Provides
    fun playlistTracksUseCase(
            repository: ISpotifyRepository
    ): GetPlaylistTracks = GetPlaylistTracks(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun searchAllUseCase(repository: ISpotifyRepository): SearchSpotify = SearchSpotify(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun albumUseCase(
            repository: ISpotifyRepository
    ): GetAlbum = GetAlbum(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun similarTracksUseCase(
            repository: ISpotifyRepository
    ): GetSimilarTracks = GetSimilarTracks(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun insertTrackUseCase(
            repository: ISpotifyRepository
    ): InsertTrack = InsertTrack(AsyncCompletableTransformer(), repository)

    @Provides
    fun getNewReleases(
            repository: ISpotifyRepository
    ): GetNewReleases = GetNewReleases(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun getCurrentUsersPlaylists(
            repository: ISpotifyRepository
    ): GetCurrentUsersPlaylists = GetCurrentUsersPlaylists(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun getCurrentUsersTopTracks(
            repository: ISpotifyRepository
    ): GetCurrentUsersTopTracks = GetCurrentUsersTopTracks(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun getCurrentUsersTopArtists(
            repository: ISpotifyRepository
    ): GetCurrentUsersTopArtists = GetCurrentUsersTopArtists(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun getCurrentUsersSavedTracks(
            repository: ISpotifyRepository
    ): GetCurrentUsersSavedTracks = GetCurrentUsersSavedTracks(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun getCurrentUsersSavedAlbums(
            repository: ISpotifyRepository
    ): GetCurrentUsersSavedAlbums = GetCurrentUsersSavedAlbums(AsyncSymmetricSingleTransformer(), repository)
}