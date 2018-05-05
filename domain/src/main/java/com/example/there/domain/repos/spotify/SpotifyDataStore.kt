package com.example.there.domain.repos.spotify

import com.example.there.domain.entities.spotify.*
import io.reactivex.Observable

interface SpotifyDataStore {
    fun getAccessToken(clientId: String, clientSecret: String): Observable<AccessTokenEntity>

    fun getCategories(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>>

    fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>>

    fun getTrack(accessToken: AccessTokenEntity, id: String): Observable<TrackEntity>

    fun getDailyViralTracks(accessToken: AccessTokenEntity): Observable<List<TopTrackEntity>>

    fun searchAll(accessToken: AccessTokenEntity, query: String): Observable<SearchAllEntity>

    fun getPlaylistsForCategory(accessToken: AccessTokenEntity, categoryId: String): Observable<List<PlaylistEntity>>

    fun getPlaylistTracks(accessToken: AccessTokenEntity, playlistId: String, userId: String): Observable<List<TrackEntity>>
}