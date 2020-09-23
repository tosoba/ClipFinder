package com.clipfinder.spotify.api.endpoint

import com.clipfinder.spotify.api.model.ErrorResponse
import com.clipfinder.spotify.api.model.TracksPagingObject
import com.example.core.retrofit.NetworkResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PersonalizationEndpoints {
    /**
     * Get a User&#39;s Top Artists and Tracks
     * Get the current user’s top artists or tracks based on calculated affinity.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a paging object of Artists or Tracks. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The access token must have been issued on behalf of the current user. Getting details of a user’s top artists and tracks requires authorization of the user-top-read scope. See Using Scopes.
     * @param type The type of entity to return. Valid values: artists or tracks
     * @param limit The number of entities to return. Default: 20. Minimum: 1. Maximum: 50. For example: limit&#x3D;2 (optional)
     * @param offset The index of the first entity to return. Default: 0 (i.e., the first track). Use with limit to get the next set of entities. (optional)
     * @param timeRange Over what time frame the affinities are computed. Valid values: long_term (calculated from several years of data and including all new data as it becomes available), medium_term (approximately last 6 months), short_term (approximately last 4 weeks). Default: medium_term (optional)
     * @return [Call]<[TracksPagingObject]>
     */
    @GET("me/top/{type}")
    fun getUsersTopArtistsAndTracks(@Header("Authorization") authorization: String? = null, @Path("type") type: String, @Query("limit") limit: Int? = null, @Query("offset") offset: Int? = null, @Query("time_range") timeRange: String? = null): Single<NetworkResponse<TracksPagingObject, ErrorResponse>>
}
