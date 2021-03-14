package com.clipfinder.core.spotify.di

import com.clipfinder.core.spotify.usecase.*
import org.koin.dsl.module

val spotifyCoreModule = module {
    single { GetAlbum(get(), get()) }
    single { GetArtists(get(), get()) }
    single { GetSimilarTracks(get(), get()) }
    single { GetAudioFeatures(get(), get()) }
    single { GetCurrentUser(get(), get()) }
    single { GetCategories(get(), get()) }
    single { GetFeaturedPlaylists(get(), get()) }
    single { GetNewReleases(get(), get()) }
    single { GetDailyViralTracks(get(), get()) }
    single { GetPlaylistsForCategory(get(), get()) }
    single { GetTracksFromAlbum(get(), get()) }
    single { GetAlbumsFromArtist(get(), get()) }
    single { GetRelatedArtists(get(), get()) }
    single { GetTopTracksFromArtist(get(), get()) }
    single { SearchSpotify(get(), get()) }
    single { GetPlaylistTracks(get(), get()) }
    single { GetTrack(get(), get()) }
    single { GetCurrentUsersTopArtists(get(), get()) }
    single { GetCurrentUsersTopTracks(get(), get()) }
    single { GetCurrentUsersSavedAlbums(get(), get()) }
    single { GetCurrentUsersSavedTracks(get(), get()) }
    single { GetCurrentUsersPlaylists(get(), get()) }
}
