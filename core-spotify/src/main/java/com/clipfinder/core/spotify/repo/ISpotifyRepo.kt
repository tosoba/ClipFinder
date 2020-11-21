package com.clipfinder.core.spotify.repo

import com.clipfinder.core.spotify.model.*
import com.example.core.model.Paged
import com.example.core.model.Resource
import io.reactivex.Single

interface ISpotifyRepo {
    fun getAlbum(id: String): Single<Resource<ISpotifySimplifiedAlbum>>
    fun getArtists(ids: List<String>): Single<Resource<List<ISpotifyArtist>>>
    fun getSimilarTracks(id: String, offset: Int): Single<Resource<Paged<List<ISpotifyTrack>>>>
    fun getAudioFeatures(id: String): Single<Resource<ISpotifyAudioFeatures>>
    val authorizedUser: Single<Resource<ISpotifyPrivateUser>>
    fun getCategories(offset: Int): Single<Resource<Paged<List<ISpotifyCategory>>>>
    fun getFeaturedPlaylists(offset: Int): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>>
    fun getDailyViralTracks(offset: Int): Single<Resource<Paged<List<ISpotifyTrack>>>>
    fun getNewReleases(offset: Int): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>>
    fun getPlaylistsForCategory(categoryId: String, offset: Int): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>>
    fun getTracksFromAlbum(albumId: String, offset: Int): Single<Resource<Paged<List<ISpotifyTrack>>>>
    fun getAlbumsFromArtist(artistId: String, offset: Int): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>>
    fun getRelatedArtists(artistId: String): Single<Resource<List<ISpotifyArtist>>>
    fun getTopTracksFromArtist(artistId: String): Single<Resource<List<ISpotifyTrack>>>
    fun search(query: String, offset: Int, type: String): Single<Resource<SpotifySearchResult>>
    fun getPlaylistTracks(playlistId: String, offset: Int): Single<Resource<Paged<List<ISpotifyTrack>>>>
    fun getCurrentUsersTopArtists(offset: Int): Single<Resource<Paged<List<ISpotifyArtist>>>>
    fun getCurrentUsersTopTracks(offset: Int): Single<Resource<Paged<List<ISpotifyTrack>>>>
    fun getCurrentUsersSavedTracks(offset: Int): Single<Resource<Paged<List<ISpotifyTrack>>>>
    fun getCurrentUsersSavedAlbums(offset: Int): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>>
    fun getCurrentUsersPlaylists(offset: Int): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>>
}