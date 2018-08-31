package com.example.there.findclips.di.modules

import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.usecases.spotify.*
import com.example.there.domain.usecases.videos.*
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun accessTokenUseCase(repository: ISpotifyRepository): GetAccessToken = GetAccessToken(AsyncTransformer(), repository)

    @Provides
    fun artistsUseCase(repository: ISpotifyRepository): GetArtists = GetArtists(AsyncTransformer(), repository)

    @Provides
    fun getChannelsThumbnailUrlsUseCase(repository: IVideosRepository): GetChannelsThumbnailUrls =
            GetChannelsThumbnailUrls(AsyncTransformer(), repository)

    @Provides
    fun getFavouriteVideoPlaylistsUseCase(repository: IVideosRepository): GetFavouriteVideoPlaylists =
            GetFavouriteVideoPlaylists(AsyncTransformer(), repository)

    @Provides
    fun insertAlbumUseCase(repository: ISpotifyRepository): InsertAlbum = InsertAlbum(AsyncTransformer(), repository)

    @Provides
    fun tracksFromAlbumUseCase(repository: ISpotifyRepository): GetTracksFromAlbum = GetTracksFromAlbum(AsyncTransformer(), repository)

    @Provides
    fun insertArtistUseCase(repository: ISpotifyRepository): InsertArtist = InsertArtist(AsyncTransformer(), repository)

    @Provides
    fun albumsFromArtistUseCase(repository: ISpotifyRepository): GetAlbumsFromArtist = GetAlbumsFromArtist(AsyncTransformer(), repository)

    @Provides
    fun topTracksFromArtistUseCase(repository: ISpotifyRepository): GetTopTracksFromArtist = GetTopTracksFromArtist(AsyncTransformer(), repository)

    @Provides
    fun relatedArtistsUseCase(repository: ISpotifyRepository): GetRelatedArtists = GetRelatedArtists(AsyncTransformer(), repository)

    @Provides
    fun insertCategoryUseCase(repository: ISpotifyRepository): InsertCategory = InsertCategory(AsyncTransformer(), repository)

    @Provides
    fun playlistsForCategoryUseCase(repository: ISpotifyRepository): GetPlaylistsForCategory = GetPlaylistsForCategory(AsyncTransformer(), repository)

    @Provides
    fun categoriesUseCase(repository: ISpotifyRepository): GetCategories = GetCategories(AsyncTransformer(), repository)

    @Provides
    fun featuredPlaylistsUseCase(repository: ISpotifyRepository): GetFeaturedPlaylists = GetFeaturedPlaylists(AsyncTransformer(), repository)

    @Provides
    fun dailyViralTracksUseCase(repository: ISpotifyRepository): GetDailyViralTracks = GetDailyViralTracks(AsyncTransformer(), repository)

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
    fun searchRelatedVideosUseCase(repository: IVideosRepository): SearchRelatedVideos = SearchRelatedVideos(AsyncTransformer(), repository)

    @Provides
    fun addVideosToPlaylistUseCase(repository: IVideosRepository): AddVideoToPlaylist = AddVideoToPlaylist(AsyncTransformer(), repository)

    @Provides
    fun insertVideoPlaylistUseCase(repository: IVideosRepository): InsertVideoPlaylist = InsertVideoPlaylist(AsyncTransformer(), repository)

    @Provides
    fun insertSpotifyPlaylistUseCase(repository: ISpotifyRepository): InsertSpotifyPlaylist = InsertSpotifyPlaylist(AsyncTransformer(), repository)

    @Provides
    fun playlistTracksUseCase(repository: ISpotifyRepository): GetPlaylistTracks = GetPlaylistTracks(AsyncTransformer(), repository)

    @Provides
    fun searchAllUseCase(repository: ISpotifyRepository): SearchSpotify = SearchSpotify(AsyncTransformer(), repository)

    @Provides
    fun albumUseCase(repository: ISpotifyRepository): GetAlbum = GetAlbum(AsyncTransformer(), repository)

    @Provides
    fun similarTracksUseCase(repository: ISpotifyRepository): GetSimilarTracks = GetSimilarTracks(AsyncTransformer(), repository)

    @Provides
    fun insertTrackUseCase(repository: ISpotifyRepository): InsertTrack = InsertTrack(AsyncTransformer(), repository)

    @Provides
    fun searchVideosUseCase(repository: IVideosRepository): SearchVideos = SearchVideos(AsyncTransformer(), repository)

    @Provides
    fun getVideosFromPlaylistsUseCase(repository: IVideosRepository): GetFavouriteVideosFromPlaylist =
            GetFavouriteVideosFromPlaylist(AsyncTransformer(), repository)
}