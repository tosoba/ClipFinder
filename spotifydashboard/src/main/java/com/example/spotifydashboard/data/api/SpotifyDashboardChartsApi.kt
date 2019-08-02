package com.example.spotifydashboard.data.api

import io.reactivex.Observable
import retrofit2.http.GET

interface SpotifyDashboardChartsApi {
    @GET("regional/global/daily/latest/download")
    fun getDailyViralTracks(): Observable<String>
}