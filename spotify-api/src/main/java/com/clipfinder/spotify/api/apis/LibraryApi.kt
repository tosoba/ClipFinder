package com.clipfinder.spotify.api.apis

import com.clipfinder.spotify.api.models.IdsBody
import com.clipfinder.spotify.api.models.InlineResponse2002
import com.clipfinder.spotify.api.models.InlineResponse2003
import com.clipfinder.spotify.api.models.InlineResponse2004
import retrofit2.Call
import retrofit2.http.*

interface LibraryApi {
    /**
     * Check User&#39;s Saved Albums
     * Check if one or more albums is already saved in the current Spotify user’s ‘Your Music’ library.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a JSON array of true or false values, in the same order in which the ids were specified. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The user-library-read scope must have been authorized by the user.
     * @param ids A comma-separated list of the Spotify IDs for the albums. Maximum: 50 IDs.
     * @return [Call]<[List<Boolean>]>
     */
    @GET("me/albums/contains")
    fun endpointCheckUsersSavedAlbums(@Header("Authorization") authorization: String, @Query("ids") ids: String): Call<List<Boolean>>

    /**
     * Check User&#39;s Saved Shows
     * Check if one or more shows is already saved in the current Spotify user’s library.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a JSON array of trueor false values, in the same order in which the ids were specified. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The access token must have been isued on behalf of the user. The user-libary-read scope must have been authorised by the user.
     * @param ids A comma-separated list of the Spotify IDs for the shows. Maximum: 50 ids.
     * @return [Call]<[List<Boolean>]>
     */
    @GET("me/shows/contains")
    fun endpointCheckUsersSavedShows(@Header("Authorization") authorization: String, @Query("ids") ids: String): Call<List<Boolean>>

    /**
     * Check User&#39;s Saved Tracks
     * Check if one or more tracks is already saved in the current Spotify user’s ‘Your Music’ library.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a JSON array of true or false values, in the same order in which the ids were specified. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The user-library-read scope must have been authorized by the user.
     * @param ids A comma-separated list of the Spotify IDs for the tracks. Maximum: 50 IDs.
     * @return [Call]<[List<Boolean>]>
     */
    @GET("me/tracks/contains")
    fun endpointCheckUsersSavedTracks(@Header("Authorization") authorization: String, @Query("ids") ids: String): Call<List<Boolean>>

    /**
     * Get User&#39;s Saved Albums
     * Get a list of the albums saved in the current Spotify user’s ‘Your Music’ library.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an array of saved album objects (wrapped in a paging object) in JSON format. Each album object is accompanied by a timestamp (added_at) to show when it was added. There is also an etag in the header that can be used in future conditional requests. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The user-library-read scope must have been authorized by the user.
     * @param limit The maximum number of objects to return. Default: 20. Minimum: 1. Maximum: 50. (optional)
     * @param offset The index of the first object to return. Default: 0 (i.e., the first object). Use with limit to get the next set of objects. (optional)
     * @param market An ISO 3166-1 alpha-2 country code or the string from_token. Provide this parameter if you want to apply Track Relinking. (optional)
     * @return [Call]<[InlineResponse2002]>
     */
    @GET("me/albums")
    fun endpointGetUsersSavedAlbums(@Header("Authorization") authorization: String, @Query("limit") limit: String? = null, @Query("offset") offset: Int? = null, @Query("market") market: String? = null): Call<InlineResponse2002>

    /**
     * Get User&#39;s Saved Shows
     * Get a list of shows saved in the current Spotify user’s library. Optional parameters can be used to limit the number of shows returned.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an array of saved show objects (wrapped in a paging object) in JSON format. If the current user has no shows saved, the response will be an empty array. If a show is unavailable in the given market it is filtered out. The total field in the paging object represents the number of all items, filtered or not, and thus might be larger than the actual total number of observable items. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The access token must have been isued on behalf of the user. The user-libary-read scope must have been authorised by the user.
     * @param limit The maximum number of shows to return. Default: 20. Minimum: 1. Maximum: 50 (optional)
     * @param offset The index of the first show to return. Default: 0 (the first object). Use with limit to get the next set of shows. (optional)
     * @return [Call]<[InlineResponse2003]>
     */
    @GET("me/shows")
    fun endpointGetUsersSavedShows(@Header("Authorization") authorization: String, @Query("limit") limit: String? = null, @Query("offset") offset: Int? = null): Call<InlineResponse2003>

    /**
     * Get User&#39;s Saved Tracks
     * Get a list of the songs saved in the current Spotify user’s ‘Your Music’ library.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an array of saved track objects (wrapped in a paging object) in JSON format. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The user-library-read scope must have been authorized by the user.
     * @param limit The maximum number of objects to return. Default: 20. Minimum: 1. Maximum: 50. (optional)
     * @param offset The index of the first object to return. Default: 0 (i.e., the first object). Use with limit to get the next set of objects. (optional)
     * @param market An ISO 3166-1 alpha-2 country code or the string from_token. Provide this parameter if you want to apply Track Relinking. (optional)
     * @return [Call]<[InlineResponse2004]>
     */
    @GET("me/tracks")
    fun endpointGetUsersSavedTracks(@Header("Authorization") authorization: String, @Query("limit") limit: String? = null, @Query("offset") offset: Int? = null, @Query("market") market: String? = null): Call<InlineResponse2004>

    /**
     * Remove Albums for Current User
     * Remove one or more albums from the current user’s ‘Your Music’ library.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 Success. On error, the header status code is an error code and the response body contains an error object. Trying to remove an album when you do not have the user’s authorization returns error 403 Forbidden.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. Modification of the current user’s “Your Music” collection requires authorization of the user-library-modify scope.
     * @param ids A comma-separated list of the Spotify IDs. For example: ids&#x3D;4iV5W9uYEdYUVa79Axb7Rh,1301WleyT98MSxVHPZCA6M. Maximum: 50 IDs.
     * @param contentMinusType Required if the IDs are passed in the request body, otherwise ignored. The content type of the request body: application/json (optional)
     * @param idsBody  (optional)
     * @return [Call]<[Unit]>
     */
    @DELETE("me/albums")
    fun endpointRemoveAlbumsUser(@Header("Authorization") authorization: String, @Query("ids") ids: String? = null, @Header("Content-Type") contentMinusType: String, @Body idsBody: IdsBody? = null): Call<Unit>

    /**
     * Remove User&#39;s Saved Tracks
     * Delete one or more shows from current Spotify user’s library.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK. On error, the header status code is an error code and the response body contains an error object. A 403 Forbidden while trying to add a show when you do not have the user’s authorisation.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The access token must have been issued on behalf of the user. The user-library-modify scope must have been authorized by the user.
     * @param ids A comma-separated list of Spotify IDs for the shows to be deleted from the user’s library.
     * @param market An ISO 3166-1 alpha-2 country code. If a country code is specified, only shows that are available in that market will be removed. If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter. Note: If neither market or user country are provided, the content is considered unavailable for the client. Users can view the country that is associated with their account in the account settings. (optional)
     * @return [Call]<[Unit]>
     */
    @DELETE("me/shows")
    fun endpointRemoveShowsUser(@Header("Authorization") authorization: String, @Query("ids") ids: String, @Query("market") market: String? = null): Call<Unit>

    /**
     * Remove User&#39;s Saved Tracks
     * Remove one or more tracks from the current user’s ‘Your Music’ library.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 Success. On error, the header status code is an error code and the response body contains an error object. Trying to remove an album when you do not have the user’s authorization returns error 403 Forbidden.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. Modification of the current user’s “Your Music” collection requires authorization of the user-library-modify scope.
     * @param ids A comma-separated list of the Spotify IDs. For example: ids&#x3D;4iV5W9uYEdYUVa79Axb7Rh,1301WleyT98MSxVHPZCA6M. Maximum: 50 IDs.
     * @param contentMinusType Required if the IDs are passed in the request body, otherwise ignored. The content type of the request body: application/json (optional)
     * @return [Call]<[Unit]>
     */
    @DELETE("me/tracks")
    fun endpointRemoveTracksUser(@Header("Authorization") authorization: String, @Query("ids") ids: String, @Header("Content-Type") contentMinusType: String): Call<Unit>

    /**
     * Save Albums for Current User
     * Save one or more albums to the current user’s ‘Your Music’ library.
     * Responses:
     *  - 201: On success, the HTTP status code in the response header is 201 Created. On error, the header status code is an error code and the response body contains an error object. Trying to add an album when you do not have the user’s authorization returns error 403 Forbidden.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. Modification of the current user’s “Your Music” collection requires authorization of the user-library-modify scope.
     * @param ids A comma-separated list of the Spotify IDs. For example: ids&#x3D;4iV5W9uYEdYUVa79Axb7Rh,1301WleyT98MSxVHPZCA6M. Maximum: 50 IDs.
     * @param contentMinusType Required if the IDs are passed in the request body, otherwise ignored. The content type of the request body: application/json (optional)
     * @param idsBody  (optional)
     * @return [Call]<[Unit]>
     */
    @PUT("me/albums")
    fun endpointSaveAlbumsUser(@Header("Authorization") authorization: String, @Query("ids") ids: String? = null, @Header("Content-Type") contentMinusType: String, @Body idsBody: IdsBody? = null): Call<Unit>

    /**
     * Save Shows for Current User
     * Save one or more shows to current Spotify user’s library.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK. On error, the header status code is an error code and the response body contains an error object. A 403 Forbidden while trying to add a show when you do not have the user’s authorisation or when the user already has have over 10,000 items saved in library.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The access token must have been issued on behalf of the user. The user-library-modify scope must have been authorized by the user.
     * @param ids A comma-separated list of Spotify IDs for the shows to be added to the user’s library.
     * @return [Call]<[Unit]>
     */
    @PUT("me/shows")
    fun endpointSaveShowsUser(@Header("Authorization") authorization: String, @Query("ids") ids: String): Call<Unit>

    /**
     * Save Tracks for User
     * Save one or more tracks to the current user’s ‘Your Music’ library.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK. On error, the header status code is an error code and the response body contains an error object. Trying to add a track when you do not have the user’s authorization, or when you have over 10.000 tracks in Your Music, returns error 403 Forbidden.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. Modification of the current user’s “Your Music” collection requires authorization of the user-library-modify scope.
     * @param ids A comma-separated list of the Spotify IDs. For example: ids&#x3D;4iV5W9uYEdYUVa79Axb7Rh,1301WleyT98MSxVHPZCA6M. Maximum: 50 IDs.
     * @param contentMinusType Required if the IDs are passed in the request body, otherwise ignored. The content type of the request body: application/json (optional)
     * @return [Call]<[Unit]>
     */
    @PUT("me/tracks")
    fun endpointSaveTracksUser(@Header("Authorization") authorization: String, @Query("ids") ids: String, @Header("Content-Type") contentMinusType: String): Call<Unit>

}
