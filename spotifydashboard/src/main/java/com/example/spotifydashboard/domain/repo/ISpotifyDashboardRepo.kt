package com.example.spotifydashboard.domain.repo

import com.example.core.model.Resource
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.entity.spotify.TopTrackEntity
import io.reactivex.Single

interface ISpotifyDashboardRepo {
    fun getCategories(offset: Int): Single<Resource<Page<CategoryEntity>>>
    fun getFeaturedPlaylists(offset: Int): Single<Resource<Page<PlaylistEntity>>>
    fun getDailyViralTracks(offset: Int): Single<Resource<Page<TopTrackEntity>>>
    fun getNewReleases(offset: Int): Single<Resource<Page<AlbumEntity>>>
}
