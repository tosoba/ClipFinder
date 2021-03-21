package com.clipfinder.core.spotify.di

import com.clipfinder.core.spotify.usecase.*
import org.koin.dsl.module

val spotifyCoreModule = module {
    single { GetAlbum(get()) }
    single { GetArtists(get()) }
    single { GetSimilarTracks(get()) }
    single { GetAudioFeatures(get()) }
    single { GetCurrentUser(get()) }
    single { GetCategories(get()) }
    single { GetFeaturedPlaylists(get()) }
    single { GetNewReleases(get()) }
    single { GetDailyViralTracks(get()) }
    single { GetPlaylistsForCategory(get()) }
    single { GetTracksFromAlbum(get()) }
    single { GetAlbumsFromArtist(get()) }
    single { GetRelatedArtists(get()) }
    single { GetTopTracksFromArtist(get()) }
    single { SearchSpotify(get()) }
    single { GetPlaylistTracks(get()) }
    single { GetTrack(get()) }
    single { GetCurrentUsersTopArtists(get()) }
    single { GetCurrentUsersTopTracks(get()) }
    single { GetCurrentUsersSavedAlbums(get()) }
    single { GetCurrentUsersSavedTracks(get()) }
    single { GetCurrentUsersPlaylists(get()) }
}
