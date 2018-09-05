package com.example.there.findclips.di.modules

import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.usecases.spotify.*
import com.example.there.domain.usecases.videos.*
import com.example.there.findclips.util.rx.AsyncSymmetricObservableTransformer
import com.example.there.findclips.util.rx.AsyncSymmetricSingleTransformer
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun accessTokenUseCase(repository: ISpotifyRepository): GetAccessToken = GetAccessToken(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun artistsUseCase(repository: ISpotifyRepository): GetArtists = GetArtists(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun getChannelsThumbnailUrlsUseCase(repository: IVideosRepository): GetChannelsThumbnailUrls =
            GetChannelsThumbnailUrls(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun getFavouriteVideoPlaylistsUseCase(repository: IVideosRepository): GetFavouriteVideoPlaylists =
            GetFavouriteVideoPlaylists(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun insertAlbumUseCase(repository: ISpotifyRepository): InsertAlbum = InsertAlbum(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun tracksFromAlbumUseCase(repository: ISpotifyRepository): GetTracksFromAlbum = GetTracksFromAlbum(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun insertArtistUseCase(repository: ISpotifyRepository): InsertArtist = InsertArtist(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun albumsFromArtistUseCase(repository: ISpotifyRepository): GetAlbumsFromArtist = GetAlbumsFromArtist(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun topTracksFromArtistUseCase(repository: ISpotifyRepository): GetTopTracksFromArtist = GetTopTracksFromArtist(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun relatedArtistsUseCase(repository: ISpotifyRepository): GetRelatedArtists = GetRelatedArtists(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun insertCategoryUseCase(repository: ISpotifyRepository): InsertCategory = InsertCategory(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun playlistsForCategoryUseCase(
            repository: ISpotifyRepository
    ): GetPlaylistsForCategory = GetPlaylistsForCategory(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun categoriesUseCase(repository: ISpotifyRepository): GetCategories = GetCategories(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun featuredPlaylistsUseCase(repository: ISpotifyRepository): GetFeaturedPlaylists = GetFeaturedPlaylists(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun dailyViralTracksUseCase(repository: ISpotifyRepository): GetDailyViralTracks = GetDailyViralTracks(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun getFavouriteAlbumsUseCase(repository: ISpotifyRepository): GetFavouriteAlbums = GetFavouriteAlbums(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun getFavouriteArtistsUseCase(repository: ISpotifyRepository): GetFavouriteArtists = GetFavouriteArtists(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun getFavouriteCategoriesUseCase(repository: ISpotifyRepository): GetFavouriteCategories = GetFavouriteCategories(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun getFavouriteSpotifyPlaylistsUseCase(repository: ISpotifyRepository): GetFavouriteSpotifyPlaylists =
            GetFavouriteSpotifyPlaylists(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun getFavouriteTracksUseCase(repository: ISpotifyRepository): GetFavouriteTracks = GetFavouriteTracks(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun searchRelatedVideosUseCase(repository: IVideosRepository): SearchRelatedVideos = SearchRelatedVideos(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun addVideosToPlaylistUseCase(repository: IVideosRepository): AddVideoToPlaylist = AddVideoToPlaylist(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun insertVideoPlaylistUseCase(repository: IVideosRepository): InsertVideoPlaylist = InsertVideoPlaylist(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun insertSpotifyPlaylistUseCase(repository: ISpotifyRepository): InsertSpotifyPlaylist = InsertSpotifyPlaylist(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun playlistTracksUseCase(
            repository: ISpotifyRepository
    ): GetPlaylistTracks = GetPlaylistTracks(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun searchAllUseCase(repository: ISpotifyRepository): SearchSpotify = SearchSpotify(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun albumUseCase(repository: ISpotifyRepository): GetAlbum = GetAlbum(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun similarTracksUseCase(repository: ISpotifyRepository): GetSimilarTracks = GetSimilarTracks(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun insertTrackUseCase(repository: ISpotifyRepository): InsertTrack = InsertTrack(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun searchVideosUseCase(repository: IVideosRepository): SearchVideos = SearchVideos(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun getVideosFromPlaylistsUseCase(repository: IVideosRepository): GetFavouriteVideosFromPlaylist =
            GetFavouriteVideosFromPlaylist(AsyncSymmetricObservableTransformer(), repository)
}