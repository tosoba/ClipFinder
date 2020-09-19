package com.clipfinder.spotify.api.apis

import com.clipfinder.spotify.api.models.EpisodeObject
import com.clipfinder.spotify.api.models.EpisodesObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodesApi {
    /**
     * Get an Episode
     * Get Spotify catalog information for a single episode identified by its unique Spotify ID.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an episode object in JSON format. On error, the header status code is an error code and the response body contains an error object. If an episode is unavailable in the given market the HTTP status code in the response header is 404 NOT FOUND.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details.
     * @param id The Spotify ID for the episode.
     * @param market An ISO 3166-1 alpha-2 country code. If a country code is specified, only shows and episodes that are available in that market will be returned. If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter. Note: If neither market or user country are provided, the content is considered unavailable for the client. Users can view the country that is associated with their account in the account settings. (optional)
     * @return [Call]<[EpisodeObject]>
     */
    @GET("episodes/{id}")
    fun endpointGetAnEpisode(@Header("Authorization") authorization: String, @Path("id") id: String, @Query("market") market: String? = null): Call<EpisodeObject>

    /**
     * Get Multiple Episodes
     * Get Spotify catalog information for several episodes based on their Spotify IDs.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an object whose key is episodes and whose value is an array of episode objects in JSON format. Objects are returned in the order requested. If an object is not found, a null value is returned in the appropriate position. Duplicate ids in the query will result in duplicate objects in the response. If an episode is unavailable in the given market, a null value is returned. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details.
     * @param ids A comma-separated list of the Spotify IDs for the episodes. Maximum: 50 IDs.
     * @param market An ISO 3166-1 alpha-2 country code. If a country code is specified, only shows and episodes that are available in that market will be returned. If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter. Note: If neither market or user country are provided, the content is considered unavailable for the client. Users can view the country that is associated with their account in the account settings. (optional)
     * @return [Call]<[EpisodesObject]>
     */
    @GET("episodes")
    fun endpointGetMultipleEpisodes(@Header("Authorization") authorization: String, @Query("ids") ids: String, @Query("market") market: String? = null): Call<EpisodesObject>

}
