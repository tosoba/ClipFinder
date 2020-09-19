package com.clipfinder.spotify.api.apis

import com.clipfinder.spotify.api.models.SimplifiedEpisodesPagingObject
import com.clipfinder.spotify.api.models.ShowObject
import com.clipfinder.spotify.api.models.ShowsObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ShowsApi {
    /**
     * Get a Show
     * Get Spotify catalog information for a single show identified by its unique Spotify ID.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a show object in JSON format. On error, the header status code is an error code and the response body contains an error object. If a show is unavailable in the given market the HTTP status code in the response header is 404 NOT FOUND.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details.
     * @param id The Spotify ID for the show.
     * @param market An ISO 3166-1 alpha-2 country code. If a country code is specified, only shows and episodes that are available in that market will be returned. If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter. Note: If neither market or user country are provided, the content is considered unavailable for the client. Users can view the country that is associated with their account in the account settings. (optional)
     * @return [Call]<[ShowObject]>
     */
    @GET("shows/{id}")
    fun endpointGetAShow(@Header("Authorization") authorization: String, @Path("id") id: String, @Query("market") market: String? = null): Call<ShowObject>

    /**
     * Get a Show&#39;s Episodes
     * Get Spotify catalog information about an show’s episodes. Optional parameters can be used to limit the number of episodes returned.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an array of simplified episode objects (wrapped in a paging object) in JSON format. On error, the header status code is an error code and the response body contains an error object. If a show is unavailable in the given market the HTTP status code in the response header is 404 NOT FOUND. Unavailable episodes are filtered out.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details.
     * @param id The Spotify ID for the show.
     * @param limit The maximum number of episodes to return. Default: 20. Minimum: 1. Maximum: 50. (optional)
     * @param offset The index of the first episode to return. Default: 0 (the first object). Use with limit to get the next set of episodes. (optional)
     * @param market An ISO 3166-1 alpha-2 country code. If a country code is specified, only shows and episodes that are available in that market will be returned. If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter. Note: If neither market or user country are provided, the content is considered unavailable for the client. Users can view the country that is associated with their account in the account settings. (optional)
     * @return [Call]<[SimplifiedEpisodesPagingObject]>
     */
    @GET("shows/{id}/episodes")
    fun endpointGetAShowsEpisodes(@Header("Authorization") authorization: String, @Path("id") id: String, @Query("limit") limit: Int? = null, @Query("offset") offset: Int? = null, @Query("market") market: String? = null): Call<SimplifiedEpisodesPagingObject>

    /**
     * Get Multiple Shows
     * Get Spotify catalog information for several shows based on their Spotify IDs.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an object whose key is shows and whose value is an array of simple show object in JSON format. Objects are returned in the order requested. If an object is not found, a null value is returned in the appropriate position. If a show is unavailable in the given market, a null value is returned. Duplicate ids in the query will result in duplicate objects in the response. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details.
     * @param ids A comma-separated list of the Spotify IDs for the shows. Maximum: 50 IDs.
     * @param market An ISO 3166-1 alpha-2 country code. If a country code is specified, only shows and episodes that are available in that market will be returned. If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter. Note: If neither market or user country are provided, the content is considered unavailable for the client. Users can view the country that is associated with their account in the account settings. (optional)
     * @return [Call]<[ShowsObject]>
     */
    @GET("shows")
    fun endpointGetMultipleShows(@Header("Authorization") authorization: String, @Query("ids") ids: String, @Query("market") market: String? = null): Call<ShowsObject>

}
