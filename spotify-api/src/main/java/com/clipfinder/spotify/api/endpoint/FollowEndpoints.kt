package com.clipfinder.spotify.api.endpoint

import com.clipfinder.core.model.NetworkResponse
import com.clipfinder.spotify.api.model.ErrorResponse
import com.clipfinder.spotify.api.model.FollowingArtistsObject
import com.clipfinder.spotify.api.model.IdsBody
import com.clipfinder.spotify.api.model.PublicBody
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface FollowEndpoints {
    /**
     * Get Following State for Artists/Users
     * Check to see if the current user is following one or more artists or other Spotify users.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a JSON array of true or false values, in the same order in which the ids were specified. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials. Requires the user-follow-read scope.
     * @param type The ID type: either artist or user.
     * @param ids A comma-separated list of the artist or the user Spotify IDs to check. For example: ids&#x3D;74ASZWbe4lXaubB36ztrGX,08td7MxkoHQkXnWAYD8d6Q. A maximum of 50 IDs can be sent in one request.
     * @return [Call]<[List<Boolean>]>
     */
    @GET("me/following/contains")
    fun checkCurrentUserFollows(@Header("Authorization") authorization: String? = null, @Query("type") type: String, @Query("ids") ids: String): Single<NetworkResponse<List<Boolean>, ErrorResponse>>

    /**
     * Check if Users Follow a Playlist
     * Check to see if one or more Spotify users are following a specified playlist.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a JSON array of true or false values, in the same order in which the ids were specified. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials. Requires the playlist-read-private scope if a private playlist is requested.
     * @param playlistId The Spotify ID of the playlist.
     * @param ids A comma-separated list of Spotify User IDs ; the ids of the users that you want to check to see if they follow the playlist. Maximum: 5 ids.
     * @return [Call]<[List<Boolean>]>
     */
    @GET("playlists/{playlist_id}/followers/contains")
    fun checkIfUserFollowsPlaylist(@Header("Authorization") authorization: String? = null, @Path("playlist_id") playlistId: String, @Query("ids") ids: String): Single<NetworkResponse<List<Boolean>, ErrorResponse>>

    /**
     * Follow Artists or Users
     * Add the current user as a follower of one or more artists or other Spotify users.
     * Responses:
     *  - 204: On success, the HTTP status code in the response header is 204 No Content and the response body is empty. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials. Requires the user-follow-modify scope.
     * @param type The ID type: either artist or user.
     * @param ids A comma-separated list of the artist or the user Spotify IDs. For example: ids&#x3D;74ASZWbe4lXaubB36ztrGX,08td7MxkoHQkXnWAYD8d6Q. A maximum of 50 IDs can be sent in one request.
     * @param idsBody
     * @param contentMinusType Required if IDs are passed in the request body, otherwise ignored. The content type of the request body: application/json (optional)
     * @return [Call]<[Unit]>
     */
    @PUT("me/following")
    fun followArtistsUsers(@Header("Authorization") authorization: String? = null, @Query("type") type: String, @Query("ids") ids: String? = null, @Body idsBody: IdsBody? = null, @Header("Content-Type") contentMinusType: String): Completable

    /**
     * Follow a Playlist
     * Add the current user as a follower of a playlist.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body is empty. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials. Requires the user-follow-modify scope.
     * @param contentMinusType The content type of the request body: application/json
     * @param playlistId The Spotify ID of the playlist. Any playlist can be followed, regardless of its public/private status, as long as you know its playlist ID.
     * @param publicBody  (optional)
     * @return [Call]<[Unit]>
     */
    @PUT("playlists/{playlist_id}/followers")
    fun followPlaylist(@Header("Authorization") authorization: String? = null, @Header("Content-Type") contentMinusType: String, @Path("playlist_id") playlistId: String, @Body publicBody: PublicBody? = null): Completable

    /**
     * Get User&#39;s Followed Artists
     * Get the current user’s followed artists.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an artists object. The artists object in turn contains a cursor-based paging object of Artists. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials. Requires the user-follow-modify scope.
     * @param type The ID type: currently only artist is supported.
     * @param limit The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50. (optional)
     * @param after The last artist ID retrieved from the previous request. (optional)
     * @return [Call]<[FollowingArtistsObject]>
     */
    @GET("me/following")
    fun getFollowed(@Header("Authorization") authorization: String? = null, @Query("type") type: String, @Query("limit") limit: String? = null, @Query("after") after: String? = null): Single<NetworkResponse<FollowingArtistsObject, ErrorResponse>>

    /**
     * Unfollow Artists or Users
     * Remove the current user as a follower of one or more artists or other Spotify users.
     * Responses:
     *  - 204: On success, the HTTP status code in the response header is 204 No Content and the response body is empty. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials. Requires the user-follow-modify scope.
     * @param type The ID type: either artist or user.
     * @param ids A comma-separated list of the artist or the user Spotify IDs. For example: ids&#x3D;74ASZWbe4lXaubB36ztrGX,08td7MxkoHQkXnWAYD8d6Q. A maximum of 50 IDs can be sent in one request.
     * @param contentMinusType Required if IDs are passed in the request body, otherwise ignored. The content type of the request body: application/json. (optional)
     * @param idsBody  (optional)
     * @return [Call]<[Unit]>
     */
    @DELETE("me/following")
    fun unfollowArtistsUsers(@Header("Authorization") authorization: String? = null, @Query("type") type: String, @Query("ids") ids: String? = null, @Header("Content-Type") contentMinusType: String, @Body idsBody: IdsBody? = null): Completable

    /**
     * Unfollow Playlist
     * Remove the current user as a follower of a playlist.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body is empty. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The access token must have been issued on behalf of the user. Unfollowing a publicly followed playlist for a user requires authorization of the playlist-modify-public scope; unfollowing a privately followed playlist requires the playlist-modify-private scope. See Using Scopes. Note that the scopes you provide relate only to whether the current user is following the playlist publicly or privately (i.e. showing others what they are following), not whether the playlist itself is public or private.
     * @param playlistId The Spotify ID of the playlist that is to be no longer followed.
     * @return [Call]<[Unit]>
     */
    @DELETE("playlists/{playlist_id}/followers")
    fun unfollowPlaylist(@Header("Authorization") authorization: String? = null, @Path("playlist_id") playlistId: String): Completable
}
