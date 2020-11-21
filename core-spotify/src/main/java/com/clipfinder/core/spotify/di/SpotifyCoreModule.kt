package com.clipfinder.core.spotify.di

import com.clipfinder.core.spotify.usecase.*
import org.koin.dsl.module

val spotifyCoreModule = module {
    single { GetAlbum(get(), get(), get()) }
    single { GetArtists(get(), get(), get()) }
    single { GetSimilarTracks(get(), get(), get()) }
    single { GetAudioFeatures(get(), get()) }
    single { GetCurrentUser(get(), get(), get()) }
    single { GetCategories(get(), get(), get()) }
    single { GetFeaturedPlaylists(get(), get(), get()) }
    single { GetNewReleases(get(), get(), get()) }
    single { GetDailyViralTracks(get(), get(), get()) }
    single { GetPlaylistsForCategory(get(), get(), get()) }
    single { GetTracksFromAlbum(get(), get(), get()) }
    single { GetAlbumsFromArtist(get(), get(), get()) }
    single { GetRelatedArtists(get(), get(), get()) }
    single { GetTopTracksFromArtist(get(), get(), get()) }
    single { SearchSpotify(get(), get(), get()) }
    single { GetPlaylistTracks(get(), get(), get()) }
    single { GetCurrentUsersTopArtists(get(), get(), get()) }
    single { GetCurrentUsersTopTracks(get(), get(), get()) }
    single { GetCurrentUsersSavedAlbums(get(), get(), get()) }
    single { GetCurrentUsersSavedTracks(get(), get(), get()) }
    single { GetCurrentUsersPlaylists(get(), get(), get()) }
}
