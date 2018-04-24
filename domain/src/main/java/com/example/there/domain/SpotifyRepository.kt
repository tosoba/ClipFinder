package com.example.there.domain

import com.example.there.domain.entities.AccessTokenEntity
import com.example.there.domain.entities.CategoryEntity
import com.example.there.domain.entities.PlaylistEntity
import io.reactivex.Observable

interface SpotifyRepository {
    fun getCategories(accessToken: AccessTokenEntity): Observable<List<CategoryEntity>>

    fun getAccessToken(clientId: String, clientSecret: String): Observable<AccessTokenEntity>

    fun getFeaturedPlaylists(accessToken: AccessTokenEntity): Observable<List<PlaylistEntity>>
}