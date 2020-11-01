package com.clipfinder.spotify.api.charts

import io.reactivex.Single
import retrofit2.http.GET

interface SpotifyChartsApi {
    @GET("regional/global/daily/latest/download")
    fun getDailyViralTracks(): Single<String>
}
