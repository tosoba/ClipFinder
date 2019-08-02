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
    val categories: Observable<Resource<List<CategoryEntity>>>
    val featuredPlaylists: Observable<Resource<List<PlaylistEntity>>>
    val dailyViralTracks: Observable<Resource<List<TopTrackEntity>>>
    fun getNewReleases(offset: Int): Single<Resource<ListPage<AlbumEntity>>>
}