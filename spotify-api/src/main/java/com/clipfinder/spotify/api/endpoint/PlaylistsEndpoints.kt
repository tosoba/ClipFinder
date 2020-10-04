package com.clipfinder.spotify.api.endpoint

import com.clipfinder.spotify.api.model.*
import com.example.core.retrofit.NetworkResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface PlaylistsEndpoints {
    /**
     * Add Items to a Playlist
     * Add one or more items to a user’s playlist.
     * Responses:
     *  - 201: On success, the HTTP status code in the response header is 201 Created. The response body contains a snapshot_id in JSON format. The snapshot_id can be used to identify your playlist version in future requests. On error, the header status code is an error code and the response body contains an error object. Trying to add an item when you do not have the user’s authorization, or when there are more than 10.000 items in the playlist, returns error 403 Forbidden.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The access token must have been issued on behalf of the user. Adding items to the current user’s public playlists requires authorization of the playlist-modify-public scope; adding items to the current user’s private playlist (including collaborative playlists) requires the playlist-modify-private scope. See Using Scopes.
     * @param contentMinusType Required if URIs are passed in the request body, otherwise ignored. The content type of the request body: application/json
     * @param playlistId The Spotify ID for the playlist.
     * @param uris A comma-separated list of Spotify URIs to add, can be track or episode URIs. For example: uris&#x3D;spotify:track:4iV5W9uYEdYUVa79Axb7Rh, spotify:track:1301WleyT98MSxVHPZCA6M, spotify:episode:512ojhOuo1ktJprKbVcKyQ A maximum of 100 items can be added in one request. Note: it is likely that passing a large number of item URIs as a query parameter will exceed the maximum length of the request URI. When adding a large number of items, it is recommended to pass them in the request body, see below. (optional)
     * @param position The position to insert the items, a zero-based index. For example, to insert the items in the first position: position&#x3D;0; to insert the items in the third position: position&#x3D;2 . If omitted, the items will be appended to the playlist. Items are added in the order they are listed in the query string or request body. (optional)
     * @param urisPositionBody  (optional)
     * @return [Call]<[SnapshotIdObject]>
     */
    @POST("playlists/{playlist_id}/tracks")
    fun addTracksToPlaylist(@Header("Authorization") authorization: String? = null, @Header("Content-Type") contentMinusType: String, @Path("playlist_id") playlistId: String, @Query("uris") uris: String? = null, @Query("position") position: Int? = null, @Body urisPositionBody: UrisPositionBody? = null): Single<NetworkResponse<SnapshotIdObject, ErrorResponse>>

    /**
     * Change a Playlist&#39;s Details
     * Change a playlist’s name and public/private state. (The user must, of course, own the playlist.)
     * Responses:
     *  - 200: On success the HTTP status code in the response header is 200 OK. On error, the header status code is an error code and the response body contains an error object. Trying to change a playlist when you do not have the user’s authorization returns error 403 Forbidden.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The access token must have been issued on behalf of the user. Changing a public playlist for a user requires authorization of the playlist-modify-public scope; changing a private playlist requires the playlist-modify-private scope. See Using Scopes.
     * @param contentMinusType The content type of the request body: application/json
     * @param playlistId The Spotify ID for the playlist.
     * @param playlistDetailsBody  (optional)
     * @return [Call]<[Unit]>
     */
    @PUT("playlists/{playlist_id}")
    fun changePlaylistDetails(@Header("Authorization") authorization: String? = null, @Header("Content-Type") contentMinusType: String, @Path("playlist_id") playlistId: String, @Body playlistDetailsBody: PlaylistDetailsBody? = null): Completable

    /**
     * Create a Playlist
     * Create a playlist for a Spotify user. (The playlist will be empty until you add tracks.)
     * Responses:
     *  - 200: On success, the response body contains the created playlist object in JSON format and the HTTP status code in the response header is 200 OK or 201 Created. There is also a Location response header giving the Web API endpoint for the new playlist. On error, the header status code is an error code and the response body contains an error object. Trying to create a playlist when you do not have the user’s authorization returns error 403 Forbidden.
     *  - 201: On success, the response body contains the created playlist object in JSON format and the HTTP status code in the response header is 200 OK or 201 Created. There is also a Location response header giving the Web API endpoint for the new playlist. On error, the header status code is an error code and the response body contains an error object. Trying to create a playlist when you do not have the user’s authorization returns error 403 Forbidden.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The access token must have been issued on behalf of the user. Creating a public playlist for a user requires authorization of the playlist-modify-public scope; creating a private playlist requires the playlist-modify-private scope. See Using Scopes.
     * @param contentMinusType The content type of the request body: application/json
     * @param userId The user’s Spotify user ID.
     * @param playlistDetailsBody
     * @return [Call]<[PlaylistObject]>
     */
    @POST("users/{user_id}/playlists")
    fun createPlaylist(@Header("Authorization") authorization: String? = null, @Header("Content-Type") contentMinusType: String, @Path("user_id") userId: String, @Body playlistDetailsBody: PlaylistDetailsBody): Single<NetworkResponse<PlaylistObject, ErrorResponse>>

    /**
     * Get a List of Current User&#39;s Playlists
     * Get a list of the playlists owned or followed by the current Spotify user.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an array of simplified playlist objects (wrapped in a paging object) in JSON format. On error, the header status code is an error code and the response body contains an error object. Please note that the access token has to be tied to a user.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. Private playlists are only retrievable for the current user and requires the playlist-read-private scope to have been authorized by the user. Note that this scope alone will not return collaborative playlists, even though they are always private. Collaborative playlists are only retrievable for the current user and requires the playlist-read-collaborative scope to have been authorized by the user.
     * @param limit The maximum number of playlists to return. Default: 20. Minimum: 1. Maximum: 50. (optional)
     * @param offset The index of the first playlist to return. Default: 0 (the first object). Maximum offset: 100.000. Use with limit to get the next set of playlists. (optional)
     * @return [Call]<[SimplifiedPlaylistsPagingObject]>
     */
    @GET("me/playlists")
    fun getAListOfCurrentUsersPlaylists(@Header("Authorization") authorization: String? = null, @Query("limit") limit: Int? = null, @Query("offset") offset: Int? = null): Single<NetworkResponse<SimplifiedPlaylistsPagingObject, ErrorResponse>>

    /**
     * Get a List of a User&#39;s Playlists
     * Get a list of the playlists owned or followed by a Spotify user.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an array of simplified playlist objects (wrapped in a paging object) in JSON format. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. Private playlists are only retrievable for the current user and requires the playlist-read-private scope to have been authorized by the user. Note that this scope alone will not return collaborative playlists, even though they are always private. Collaborative playlists are only retrievable for the current user and requires the playlist-read-collaborative scope to have been authorized by the user.
     * @param userId The user’s Spotify user ID.
     * @param limit The maximum number of playlists to return. Default: 20. Minimum: 1. Maximum: 50. (optional)
     * @param offset The index of the first playlist to return. Default: 0 (the first object). Maximum offset: 100.000. Use with limit to get the next set of playlists. (optional)
     * @return [Call]<[SimplifiedPlaylistsPagingObject]>
     */
    @GET("users/{user_id}/playlists")
    fun getListUsersPlaylists(@Header("Authorization") authorization: String? = null, @Path("user_id") userId: String, @Query("limit") limit: Int? = null, @Query("offset") offset: Int? = null): Single<NetworkResponse<SimplifiedPlaylistsPagingObject, ErrorResponse>>

    /**
     * Get a Playlist
     * Get a playlist owned by a Spotify user.
     * Responses:
     *  - 200: On success, the response body contains a playlist object in JSON format and the HTTP status code in the response header is 200 OK. If an episode is unavailable in the given market, its information will not be included in the response. On error, the header status code is an error code and the response body contains an error object. Requesting playlists that you do not have the user’s authorization to access returns error 403 Forbidden.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. Both Public and Private playlists belonging to any user are retrievable on provision of a valid access token.
     * @param playlistId The Spotify ID for the playlist.
     * @param fields Filters for the query: a comma-separated list of the fields to return. If omitted, all fields are returned. For example, to get just the playlist’s description and URI: fields&#x3D;description,uri. A dot separator can be used to specify non-reoccurring fields, while parentheses can be used to specify reoccurring fields within objects. For example, to get just the added date and user ID of the adder: fields&#x3D;tracks.items(added_at,added_by.id). Use multiple parentheses to drill down into nested objects, for example: fields&#x3D;tracks.items(track(name,href,album(name,href))). Fields can be excluded by prefixing them with an exclamation mark, for example: fields&#x3D;tracks.items(track(name,href,album(!name,href))) (optional)
     * @param market An ISO 3166-1 alpha-2 country code or the string from_token. Provide this parameter if you want to apply Track Relinking. For episodes, if a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter. Note: If neither market or user country are provided, the episode is considered unavailable for the client. (optional)
     * @param additionalTypes A comma-separated list of item types that your client supports besides the default track type. Valid types are: track and episode. Note: This parameter was introduced to allow existing clients to maintain their current behaviour and might be deprecated in the future. In addition to providing this parameter, make sure that your client properly handles cases of new types in the future by checking against the type field of each object. (optional)
     * @return [Call]<[PlaylistObject]>
     */
    @GET("playlists/{playlist_id}")
    fun getPlaylist(@Header("Authorization") authorization: String? = null, @Path("playlist_id") playlistId: String, @Query("fields") fields: String? = null, @Query("market") market: String? = null, @Query("additional_types") additionalTypes: String? = null): Single<NetworkResponse<PlaylistObject, ErrorResponse>>

    /**
     * Get a Playlist Cover Image
     * Get the current image associated with a specific playlist.
     * Responses:
     *  - 200: On success, the response body contains a list of image objects in JSON format and the HTTP status code in the response header is 200 OK On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The access token must have been issued on behalf of the user. This access token must be issued on behalf of the user. Current playlist image for both Public and Private playlists of any user are retrievable on provision of a valid access token.
     * @param playlistId The Spotify ID for the playlist.
     * @return [Call]<[List<ImageObject>]>
     */
    @GET("playlists/{playlist_id}/images")
    fun getPlaylistCover(@Header("Authorization") authorization: String? = null, @Path("playlist_id") playlistId: String): Single<NetworkResponse<List<ImageObject>, ErrorResponse>>

    /**
     * Get a Playlist&#39;s Items
     * Get full details of the items of a playlist owned by a Spotify user.
     * Responses:
     *  - 200: On success, the response body contains an array of track objects and episode objects (depends on the additional_types parameter), wrapped in a paging object in JSON format and the HTTP status code in the response header is 200 OK. If an episode is unavailable in the given market, its information will not be included in the response. On error, the header status code is an error code and the response body contains an error object. Requesting playlists that you do not have the user’s authorization to access returns error 403 Forbidden.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. Both Public and Private playlists belonging to any user are retrievable on provision of a valid access token.
     * @param playlistId The Spotify ID for the playlist.
     * @param market An ISO 3166-1 alpha-2 country code or the string from_token. Provide this parameter if you want to apply Track Relinking. For episodes, if a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter. Note: If neither market or user country are provided, the episode is considered unavailable for the client.
     * @param fields Filters for the query: a comma-separated list of the fields to return. If omitted, all fields are returned. For example, to get just the total number of items and the request limit: fields&#x3D;total,limit A dot separator can be used to specify non-reoccurring fields, while parentheses can be used to specify reoccurring fields within objects. For example, to get just the added date and user ID of the adder: fields&#x3D;items(added_at,added_by.id) Use multiple parentheses to drill down into nested objects, for example: fields&#x3D;items(track(name,href,album(name,href))) Fields can be excluded by prefixing them with an exclamation mark, for example: fields&#x3D;items.track.album(!external_urls,images) (optional)
     * @param limit The maximum number of items to return. Default: 100. Minimum: 1. Maximum: 100. (optional)
     * @param offset The index of the first item to return. Default: 0 (the first object). (optional)
     * @param additionalTypes A comma-separated list of item types that your client supports besides the default track type. Valid types are: track and episode. Note: This parameter was introduced to allow existing clients to maintain their current behaviour and might be deprecated in the future. In addition to providing this parameter, make sure that your client properly handles cases of new types in the future by checking against the type field of each object. (optional)
     * @return [Call]<[TrackOrEpisodePagingObject]>
     */
    @GET("playlists/{playlist_id}/tracks")
    fun getPlaylistsTracks(@Header("Authorization") authorization: String? = null, @Path("playlist_id") playlistId: String, @Query("market") market: String? = null, @Query("fields") fields: String? = null, @Query("limit") limit: Int? = null, @Query("offset") offset: Int? = null, @Query("additional_types") additionalTypes: String? = null): Single<NetworkResponse<TrackOrEpisodePagingObject, ErrorResponse>>

    /**
     * Remove Items from a Playlist
     * Remove one or more items from a user’s playlist.
     * Responses:
     *  - 200: On success, the response body contains a snapshot_id in JSON format and the HTTP status code in the response header is 200 OK. The snapshot_id can be used to identify your playlist version in future requests. On error, the header status code is an error code and the response body contains an error object. Trying to remove an item when you do not have the user’s authorization returns error 403 Forbidden. Attempting to use several different ways to remove items returns 400 Bad Request. Other client errors returning 400 Bad Request include specifying invalid positions.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The access token must have been issued on behalf of the user. Removing items from a user’s public playlist requires authorization of the playlist-modify-public scope; removing items from a private playlist requires the playlist-modify-private scope. See Using Scopes.
     * @param contentMinusType The content type of the request body: application/json
     * @param playlistId The Spotify ID for the playlist.
     * @param removeTracksBody
     * @return [Call]<[SnapshotIdObject]>
     */
    @DELETE("playlists/{playlist_id}/tracks")
    fun removeTracksPlaylist(@Header("Authorization") authorization: String? = null, @Header("Content-Type") contentMinusType: String, @Path("playlist_id") playlistId: String, @Body removeTracksBody: RemoveTracksBody): Single<NetworkResponse<SnapshotIdObject, ErrorResponse>>

    /**
     * Replace a Playlist&#39;s Items
     * Replace all the items in a playlist, overwriting its existing items. This powerful request can be useful for replacing items, re-ordering existing items, or clearing the playlist.
     * Responses:
     *  - 201: On success, the HTTP status code in the response header is 201 Created. On error, the header status code is an error code, the response body contains an error object, and the existing playlist is unmodified. Trying to set an item when you do not have the user’s authorization returns error 403 Forbidden.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The access token must have been issued on behalf of the user. Setting items in the current user’s public playlists requires authorization of the playlist-modify-public scope; setting items in the current user’s private playlist (including collaborative playlists) requires the playlist-modify-private scope. See Using Scopes.
     * @param contentMinusType if URIs are passed in the request body, otherwise ignored._ The content type of the request body: application/json
     * @param playlistId The Spotify ID for the playlist.
     * @param uris A comma-separated list of Spotify URIs to set, can be track or episode URIs. For example: uris&#x3D;spotify:track:4iV5W9uYEdYUVa79Axb7Rh,spotify:track:1301WleyT98MSxVHPZCA6M,spotify:episode:512ojhOuo1ktJprKbVcKyQ A maximum of 100 items can be set in one request. (optional)
     * @param urisBody  (optional)
     * @return [Call]<[Unit]>
     */
    @PUT("playlists/{playlist_id}/tracks")
    fun replacePlaylistsTracks(@Header("Authorization") authorization: String? = null, @Header("Content-Type") contentMinusType: String, @Path("playlist_id") playlistId: String, @Query("uris") uris: String? = null, @Body urisBody: UrisBody? = null): Completable

    /**
     * Upload a Custom Playlist Cover Image
     * Replace the image used to represent a specific playlist.
     * Responses:
     *  - 202: If you get status code 429, it means that you have sent too many requests. If this happens, have a look in the Retry-After header, where you will see a number displayed. This is the amount of seconds that you need to wait, before you can retry sending your requests.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The access token must have been issued on behalf of the user. This access token must be tied to the user who owns the playlist, and must have the scope ugc-image-upload granted. In addition, the token must also contain playlist-modify-public and/or playlist-modify-private, depending the public status of the playlist you want to update . See Using Scopes.
     * @param contentMinusType The content type of the request body: image/jpeg
     * @param playlistId The Spotify ID for the playlist.
     * @return [Call]<[Unit]>
     */
    @PUT("playlists/{playlist_id}/images")
    fun uploadCustomPlaylistCover(@Header("Authorization") authorization: String? = null, @Header("Content-Type") contentMinusType: String, @Path("playlist_id") playlistId: String): Completable
}
