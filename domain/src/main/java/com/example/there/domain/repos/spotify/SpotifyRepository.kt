package com.example.there.domain.repos.spotify

import com.example.there.domain.entities.spotify.*
import io.reactivex.Observable

interface SpotifyRepository {
    fun getCategories(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>>

    fun getAccessToken(clientId: String, clientSecret: String): Observable<AccessTokenEntity>

    fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>>

    fun getTrack(accessToken: AccessTokenEntity, id: String): Observable<TrackEntity>

    fun getDailyViralTracks(accessToken: AccessTokenEntity): Observable<List<TopTrackEntity>>

    fun searchAll(accessToken: AccessTokenEntity, query: String): Observable<SearchAllEntity>
}