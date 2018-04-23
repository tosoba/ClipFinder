package com.example.there.domain

import com.example.there.domain.entities.CategoryEntity
import io.reactivex.Observable

interface SpotifyRepository {
    fun getCategories(accessToken: String): Observable<List<CategoryEntity>>

    fun getAccessToken(clientId: String, clientSecret: String): Observable<String>
}