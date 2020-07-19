package com.example.spotify.dashboard.domain.repo

import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.entity.spotify.TopTrackEntity
import io.reactivex.Single

interface ISpotifyDashboardRepo {
    fun getCategories(offset: Int): Single<Resource<Paged<List<CategoryEntity>>>>
    fun getFeaturedPlaylists(offset: Int): Single<Resource<Paged<List<PlaylistEntity>>>>
    fun getDailyViralTracks(offset: Int): Single<Resource<Paged<List<TopTrackEntity>>>>
    fun getNewReleases(offset: Int): Single<Resource<Paged<List<AlbumEntity>>>>
}
