package com.example.spotify.dashboard.domain.repo

import com.clipfinder.core.spotify.model.ISpotifyCategory
import com.clipfinder.core.spotify.model.ISpotifySimplifiedPlaylist
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.TopTrackEntity
import io.reactivex.Single

interface ISpotifyDashboardRepo {
    fun getCategories(offset: Int): Single<Resource<Paged<List<ISpotifyCategory>>>>
    fun getFeaturedPlaylists(offset: Int): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>>
    fun getDailyViralTracks(offset: Int): Single<Resource<Paged<List<TopTrackEntity>>>>
    fun getNewReleases(offset: Int): Single<Resource<Paged<List<AlbumEntity>>>>
}
