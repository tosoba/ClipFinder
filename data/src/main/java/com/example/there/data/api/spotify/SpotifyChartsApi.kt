package com.example.there.data.api.spotify

import io.reactivex.Observable
import retrofit2.http.GET

interface SpotifyChartsApi {
    @GET("regional/global/daily/latest/download")
    fun getDailyViralTracks(): Observable<String>
}