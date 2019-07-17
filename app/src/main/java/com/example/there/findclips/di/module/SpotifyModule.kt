package com.example.there.findclips.di.module

import com.example.there.domain.usecase.spotify.*
import org.koin.dsl.module

val spotifyModule = module {
    factory { GetAlbum(get(), get()) }
    factory { GetAlbumsFromArtist(get(), get()) }
    factory { GetArtists(get(), get()) }
    factory { GetAudioFeatures(get(), get()) }
    factory { GetCategories(get(), get()) }
    factory { GetCurrentUser(get(), get()) }
    factory { GetCurrentUsersPlaylists(get(), get()) }
    factory { GetCurrentUsersSavedAlbums(get(), get()) }
    factory { GetCurrentUsersSavedTracks(get(), get()) }
    factory { GetCurrentUsersTopArtists(get(), get()) }
    factory { GetCurrentUsersTopTracks(get(), get()) }
    factory { GetDailyViralTracks(get(), get()) }
    factory { GetFeaturedPlaylists(get(), get()) }
    factory { GetNewReleases(get(), get()) }
    factory { GetPlaylistsForCategory(get(), get()) }
    factory { GetPlaylistTracks(get(), get()) }
    factory { GetRelatedArtists(get(), get()) }
    factory { GetSimilarTracks(get(), get()) }
    factory { GetTopTracksFromArtist(get(), get()) }
    factory { GetTracksFromAlbum(get(), get()) }

    factory { GetFavouriteAlbums(get(), get()) }
    factory { InsertAlbum(get(), get()) }
    factory { IsAlbumSaved(get(), get()) }
    factory { DeleteAlbum(get(), get()) }

    factory { GetFavouriteAlbums(get(), get()) }
    factory { InsertAlbum(get(), get()) }
    factory { IsAlbumSaved(get(), get()) }
    factory { DeleteAlbum(get(), get()) }

    factory { GetFavouriteArtists(get(), get()) }
    factory { InsertArtist(get(), get()) }
    factory { IsArtistSaved(get(), get()) }
    factory { DeleteArtist(get(), get()) }

    factory { GetFavouriteCategories(get(), get()) }
    factory { InsertCategory(get(), get()) }
    factory { IsCategorySaved(get(), get()) }
    factory { DeleteCategory(get(), get()) }

    factory { GetFavouriteSpotifyPlaylists(get(), get()) }
    factory { InsertSpotifyPlaylist(get(), get()) }
    factory { IsSpotifyPlaylistSaved(get(), get()) }
    factory { DeleteSpotifyPlaylist(get(), get()) }

    factory { GetFavouriteTracks(get(), get()) }
    factory { InsertTrack(get(), get()) }
    factory { IsTrackSaved(get(), get()) }
    factory { DeleteTrack(get(), get()) }
}