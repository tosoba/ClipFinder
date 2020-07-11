package com.example.spotifydashboard.domain.repo

import com.example.core.model.Resource
import com.example.there.domain.entity.ListPage
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.entity.spotify.TopTrackEntity
import io.reactivex.Observable
import io.reactivex.Single

interface ISpotifyDashboardRemoteRepo {
    fun getCategories(offset: Int): Single<Resource<ListPage<CategoryEntity>>>
    fun getFeaturedPlaylists(offset: Int): Single<Resource<ListPage<PlaylistEntity>>>
    fun getDailyViralTracks(offset: Int): Single<Resource<ListPage<TopTrackEntity>>>
    fun getNewReleases(offset: Int): Single<Resource<ListPage<AlbumEntity>>>
}
