package com.example.there.domain

import com.example.there.domain.entities.CategoryEntity
import io.reactivex.Observable

interface SpotifyDataStore {
    fun getAccessToken(clientId: String, clientSecret: String): Observable<String>

    fun getCategories(accessToken: String): Observable<List<CategoryEntity>>
}