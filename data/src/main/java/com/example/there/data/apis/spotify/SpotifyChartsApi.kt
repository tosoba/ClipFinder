package com.example.there.data.apis.spotify

import io.reactivex.Observable
import retrofit2.http.GET

interface SpotifyChartsApi {
    @GET("viral/global/daily/latest/download")
    fun getDailyViralTracks(): Observable<String>
}