package com.clipfinder.spotify.api.charts

import io.reactivex.Single
import retrofit2.http.GET

interface ChartsEndpoints {
    @GET("regional/global/daily/latest/download") fun getDailyViralTracks(): Single<String>
}
