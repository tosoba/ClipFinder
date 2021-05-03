package com.clipfinder.spotify.api.endpoint

import com.clipfinder.core.model.NetworkResponse
import com.clipfinder.spotify.api.model.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface PlayerEndpoints {
    /**
     * Add an item to the end of the user&#39;s current playback queue. Add an item to the end of
     * the user’s current playback queue. Responses:
     * - 204: A completed request will return a 204 NO CONTENT response code, and then issue the
     * command to the player. Due to the asynchronous nature of the issuance of the command, you
     * should use the Get Information About The User’s Current Playback endpoint to check that your
     * issued command was handled correctly by the player. If the device is not found, the request
     * will return 404 NOT FOUND response code. If the user making the request is non-premium, a 403
     * FORBIDDEN response code will be returned.
     * - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API
     * Authorization Guide for details. The access token must have been issued on behalf of a user.
     * The access token must have the user-modify-playback-state scope authorized in order to
     * control playback
     * @param uri The uri of the item to add to the queue. Must be a track or an episode uri.
     * @param deviceId The id of the device this command is targeting. If not supplied, the user’s
     * currently active device is the target. (optional)
     * @return [Call]<[Unit]>
     */
    @POST("me/player/queue")
    fun addToQueue(
        @Header("Authorization") authorization: String? = null,
        @Query("uri") uri: String,
        @Query("device_id") deviceId: String? = null
    ): Completable

    /**
     * Get a User&#39;s Available Devices Get information about a user’s available devices.
     * Responses:
     * - 200: A successful request will return a 200 OK response code with a json payload that
     * contains the device objects (see below). When no available devices are found, the request
     * will return a 200 OK response with an empty devices list.
     * - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API
     * Authorization Guide for details. The access token must have been issued on behalf of a user.
     * The access token must have the user-read-playback-state scope authorized in order to read
     * information.
     * @return [Call]<[DevicesObject]>
     */
    @GET("me/player/devices")
    fun getAUsersAvailableDevices(
        @Header("Authorization") authorization: String? = null
    ): Single<NetworkResponse<DevicesObject, ErrorResponse>>

    /**
     * Get Information About The User&#39;s Current Playback Get information about the user’s
     * current playback state, including track or episode, progress, and active device. Responses:
     * - 200: A successful request will return a 200 OK response code with a json payload that
     * contains information about the current playback. The information returned is for the last
     * known state, which means an inactive device could be returned if it was the last one to
     * execute playback. When no available devices are found, the request will return a 200 OK
     * response but with no data populated.
     * - 204: A successful request will return a 200 OK response code with a json payload that
     * contains information about the current playback. The information returned is for the last
     * known state, which means an inactive device could be returned if it was the last one to
     * execute playback. When no available devices are found, the request will return a 200 OK
     * response but with no data populated.
     * - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API
     * Authorization Guide for details. The access token must have been issued on behalf of a user.
     * The access token must have the user-read-playback-state scope authorized in order to read
     * information.
     * @param market An ISO 3166-1 alpha-2 country code or the string from_token. Provide this
     * parameter if you want to apply Track Relinking. (optional)
     * @param additionalTypes A comma-separated list of item types that your client supports besides
     * the default track type. Valid types are: track and episode. An unsupported type in the
     * response is expected to be represented as null value in the item field. Note: This parameter
     * was introduced to allow existing clients to maintain their current behaviour and might be
     * deprecated in the future. In addition to providing this parameter, make sure that your client
     * properly handles cases of new types in the future by checking against the
     * currently_playing_type field. (optional)
     * @return [Call]<[CurrentPlaybackObject]>
     */
    @GET("me/player")
    fun getInformationAboutTheUsersCurrentPlayback(
        @Header("Authorization") authorization: String? = null,
        @Query("market") market: String? = null,
        @Query("additional_types") additionalTypes: String? = null
    ): Single<NetworkResponse<CurrentPlaybackObject, ErrorResponse>>

    /**
     * Get Current User&#39;s Recently Played Tracks Get tracks from the current user’s recently
     * played tracks. Note: Currently doesn’t support podcast episodes. Responses:
     * - 200: On success, the HTTP status code in the response header is 200 OK and the response
     * body contains an array of play history objects (wrapped in a cursor-based paging object) in
     * JSON format. The play history items each contain the context the track was played from (e.g.
     * playlist, album), the date and time the track was played, and a track object (simplified). On
     * error, the header status code is an error code and the response body contains an error
     * object. If private session is enabled the response will be a 204 NO CONTENT with an empty
     * payload.
     * - 204: On success, the HTTP status code in the response header is 200 OK and the response
     * body contains an array of play history objects (wrapped in a cursor-based paging object) in
     * JSON format. The play history items each contain the context the track was played from (e.g.
     * playlist, album), the date and time the track was played, and a track object (simplified). On
     * error, the header status code is an error code and the response body contains an error
     * object. If private session is enabled the response will be a 204 NO CONTENT with an empty
     * payload.
     * - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API
     * Authorization Guide for details. The access token must have been issued on behalf of a user.
     * @param limit The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50.
     * (optional)
     * @param after A Unix timestamp in milliseconds. Returns all items after (but not including)
     * this cursor position. If after is specified, before must not be specified. (optional)
     * @param before A Unix timestamp in milliseconds. Returns all items before (but not including)
     * this cursor position. If before is specified, after must not be specified. (optional)
     * @return [Call]<[PlayHistoryPagingObject]>
     */
    @GET("me/player/recently-played")
    fun getRecentlyPlayed(
        @Header("Authorization") authorization: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("after") after: Int? = null,
        @Query("before") before: Int? = null
    ): Single<NetworkResponse<PlayHistoryPagingObject, ErrorResponse>>

    /**
     * Get the User&#39;s Currently Playing Track Get the object currently being played on the
     * user’s Spotify account. Responses:
     * - 200: A successful request will return a 200 OK response code with a json payload that
     * contains information about the currently playing track or episode and its context (see
     * below). The information returned is for the last known state, which means an inactive device
     * could be returned if it was the last one to execute playback. When no available devices are
     * found, the request will return a 200 OK response but with no data populated. When no track is
     * currently playing, the request will return a 204 NO CONTENT response with no payload. If
     * private session is enabled the response will be a 204 NO CONTENT with an empty payload.
     * - 204: A successful request will return a 200 OK response code with a json payload that
     * contains information about the currently playing track or episode and its context (see
     * below). The information returned is for the last known state, which means an inactive device
     * could be returned if it was the last one to execute playback. When no available devices are
     * found, the request will return a 200 OK response but with no data populated. When no track is
     * currently playing, the request will return a 204 NO CONTENT response with no payload. If
     * private session is enabled the response will be a 204 NO CONTENT with an empty payload.
     * - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API
     * Authorization Guide for details. The access token must have been issued on behalf of a user.
     * The access token must have the user-read-currently-playing and/or user-read-playback-state
     * scope authorized in order to read information.
     * @param market An ISO 3166-1 alpha-2 country code or the string from_token. Provide this
     * parameter if you want to apply Track Relinking.
     * @param additionalTypes A comma-separated list of item types that your client supports besides
     * the default track type. Valid types are: track and episode. An unsupported type in the
     * response is expected to be represented as null value in the item field. Note: This parameter
     * was introduced to allow existing clients to maintain their current behaviour and might be
     * deprecated in the future. In addition to providing this parameter, make sure that your client
     * properly handles cases of new types in the future by checking against the
     * currently_playing_type field. (optional)
     * @return [Call]<[CurrentlyPlayingObject]>
     */
    @GET("me/player/currently-playing")
    fun getTheUsersCurrentlyPlayingTrack(
        @Header("Authorization") authorization: String? = null,
        @Query("market") market: String,
        @Query("additional_types") additionalTypes: String? = null
    ): Single<NetworkResponse<CurrentlyPlayingObject, ErrorResponse>>

    /**
     * Pause a User&#39;s Playback Pause playback on the user’s account. Responses:
     * - 204: A completed request will return a 204 NO CONTENT response code, and then issue the
     * command to the player. Due to the asynchronous nature of the issuance of the command, you
     * should use the Get Information About The User’s Current Playback endpoint to check that your
     * issued command was handled correctly by the player. If the device is not found, the request
     * will return 404 NOT FOUND response code. If the user making the request is non-premium, a 403
     * FORBIDDEN response code will be returned.
     * - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API
     * Authorization Guide for details. The access token must have been issued on behalf of a user.
     * @param deviceId The id of the device this command is targeting. If not supplied, the user’s
     * currently active device is the target. (optional)
     * @return [Call]<[Unit]>
     */
    @PUT("me/player/pause")
    fun pauseAUsersPlayback(
        @Header("Authorization") authorization: String? = null,
        @Query("device_id") deviceId: String? = null
    ): Completable

    /**
     * Seek To Position In Currently Playing Track Seeks to the given position in the user’s
     * currently playing track. Responses:
     * - 204: A completed request will return a 204 NO CONTENT response code, and then issue the
     * command to the player. Due to the asynchronous nature of the issuance of the command, you
     * should use the Get Information About The User’s Current Playback endpoint to check that your
     * issued command was handled correctly by the player. If the device is not found, the request
     * will return 404 NOT FOUND response code. If the user making the request is non-premium, a 403
     * FORBIDDEN response code will be returned.
     * - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API
     * Authorization Guide for details. The access token must have been issued on behalf of a user.
     * The access token must have the user-modify-playback-state scope authorized in order to
     * control playback
     * @param positionMs The position in milliseconds to seek to. Must be a positive number. Passing
     * in a position that is greater than the length of the track will cause the player to start
     * playing the next song.
     * @param deviceId The id of the device this command is targeting. If not supplied, the user’s
     * currently active device is the target. (optional)
     * @return [Call]<[Unit]>
     */
    @PUT("me/player/seek")
    fun seekToPositionInCurrentlyPlayingTrack(
        @Header("Authorization") authorization: String? = null,
        @Query("position_ms") positionMs: Int,
        @Query("device_id") deviceId: String? = null
    ): Completable

    /**
     * Set Repeat Mode On User’s Playback Set the repeat mode for the user’s playback. Options are
     * repeat-track, repeat-context, and off. Responses:
     * - 204: A completed request will return a 204 NO CONTENT response code, and then issue the
     * command to the player. Due to the asynchronous nature of the issuance of the command, you
     * should use the Get Information About The User’s Current Playback endpoint to check that your
     * issued command was handled correctly by the player. If the device is not found, the request
     * will return 404 NOT FOUND response code. If the user making the request is non-premium, a 403
     * FORBIDDEN response code will be returned.
     * - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API
     * Authorization Guide for details. The access token must have been issued on behalf of a user.
     * The access token must have the user-modify-playback-state scope authorized in order to
     * control playback.
     * @param state track, context or off. track will repeat the current track. context will repeat
     * the current context. off will turn repeat off.
     * @param deviceId The id of the device this command is targeting. If not supplied, the user’s
     * currently active device is the target. (optional)
     * @return [Call]<[Unit]>
     */
    @PUT("me/player/repeat")
    fun setRepeatModeOnUsersPlayback(
        @Header("Authorization") authorization: String? = null,
        @Query("state") state: String,
        @Query("device_id") deviceId: String? = null
    ): Completable

    /**
     * Set Volume For User&#39;s Playback Set the volume for the user’s current playback device.
     * Responses:
     * - 204: A completed request will return a 204 NO CONTENT response code, and then issue the
     * command to the player. Due to the asynchronous nature of the issuance of the command, you
     * should use the Get Information About The User’s Current Playback endpoint to check that your
     * issued command was handled correctly by the player. If the device is not found, the request
     * will return 404 NOT FOUND response code. If the user making the request is non-premium, a 403
     * FORBIDDEN response code will be returned.
     * - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API
     * Authorization Guide for details. The access token must have been issued on behalf of a user.
     * The access token must have the user-modify-playback-state scope authorized in order to
     * control playback.
     * @param volumePercent The volume to set. Must be a value from 0 to 100 inclusive.
     * @param deviceId The id of the device this command is targeting. If not supplied, the user’s
     * currently active device is the target. (optional)
     * @return [Call]<[Unit]>
     */
    @PUT("me/player/volume")
    fun setVolumeForUsersPlayback(
        @Header("Authorization") authorization: String? = null,
        @Query("volume_percent") volumePercent: Int,
        @Query("device_id") deviceId: String? = null
    ): Completable

    /**
     * Skip User’s Playback To Next Track Skips to next track in the user’s queue. Responses:
     * - 204: A completed request will return a 204 NO CONTENT response code, and then issue the
     * command to the player. Due to the asynchronous nature of the issuance of the command, you
     * should use the Get Information About The User’s Current Playback endpoint to check that your
     * issued command was handled correctly by the player. If the device is not found, the request
     * will return 404 NOT FOUND response code. If the user making the request is non-premium, a 403
     * FORBIDDEN response code will be returned.
     * - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API
     * Authorization Guide for details. The access token must have been issued on behalf of a user.
     * The access token must have the user-modify-playback-state scope authorized in order to
     * control playback.
     * @param deviceId The id of the device this command is targeting. If not supplied, the user’s
     * currently active device is the target. (optional)
     * @return [Call]<[Unit]>
     */
    @POST("me/player/next")
    fun skipUsersPlaybackToNextTrack(
        @Header("Authorization") authorization: String? = null,
        @Query("device_id") deviceId: String? = null
    ): Completable

    /**
     * Skip User’s Playback To Previous Track Skips to previous track in the user’s queue.
     * Responses:
     * - 204: A completed request will return a 204 NO CONTENT response code, and then issue the
     * command to the player. Due to the asynchronous nature of the issuance of the command, you
     * should use the Get Information About The User’s Current Playback endpoint to check that your
     * issued command was handled correctly by the player. If the device is not found, the request
     * will return 404 NOT FOUND response code. If the user making the request is non-premium, a 403
     * FORBIDDEN response code will be returned.
     * - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API
     * Authorization Guide for details. The access token must have been issued on behalf of a user.
     * The access token must have the user-modify-playback-state scope authorized in order to
     * control playback.
     * @param deviceId The id of the device this command is targeting. If not supplied, the user’s
     * currently active device is the target. (optional)
     * @return [Call]<[Unit]>
     */
    @POST("me/player/previous")
    fun skipUsersPlaybackToPreviousTrack(
        @Header("Authorization") authorization: String? = null,
        @Query("device_id") deviceId: String? = null
    ): Completable

    /**
     * Start/Resume a User&#39;s Playback Start a new context or resume current playback on the
     * user’s active device. Responses:
     * - 204: A completed request will return a 204 NO CONTENT response code, and then issue the
     * command to the player. Due to the asynchronous nature of the issuance of the command, you
     * should use the Get Information About The User’s Current Playback endpoint to check that your
     * issued command was handled correctly by the player. If the device is not found, the request
     * will return 404 NOT FOUND response code. If the user making the request is non-premium, a 403
     * FORBIDDEN response code will be returned.
     * - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API
     * Authorization Guide for details. The access token must have been issued on behalf of a user.
     * The access token must have the user-modify-playback-state scope authorized in order to
     * control playback.
     * @param deviceId The id of the device this command is targeting. If not supplied, the user’s
     * currently active device is the target. (optional)
     * @param startResumePlaybackBody (optional)
     * @return [Call]<[Unit]>
     */
    @PUT("me/player/play")
    fun startAUsersPlayback(
        @Header("Authorization") authorization: String? = null,
        @Query("device_id") deviceId: String? = null,
        @Body startResumePlaybackBody: StartResumePlaybackBody? = null
    ): Completable

    /**
     * Toggle Shuffle For User’s Playback Toggle shuffle on or off for user’s playback. Responses:
     * - 204: A completed request will return a 204 NO CONTENT response code, and then issue the
     * command to the player. Due to the asynchronous nature of the issuance of the command, you
     * should use the Get Information About The User’s Current Playback endpoint to check that your
     * issued command was handled correctly by the player. If the device is not found, the request
     * will return 404 NOT FOUND response code. If the user making the request is non-premium, a 403
     * FORBIDDEN response code will be returned.
     * - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API
     * Authorization Guide for details. The access token must have been issued on behalf of a user.
     * The access token must have the user-modify-playback-state scope authorized in order to
     * control playback.
     * @param state true : Shuffle user’s playback false : Do not shuffle user’s playback.
     * @param deviceId The id of the device this command is targeting. If not supplied, the user’s
     * currently active device is the target. (optional)
     * @return [Call]<[Unit]>
     */
    @PUT("me/player/shuffle")
    fun toggleShuffleForUsersPlayback(
        @Header("Authorization") authorization: String? = null,
        @Query("state") state: Boolean,
        @Query("device_id") deviceId: String? = null
    ): Completable

    /**
     * Transfer a User&#39;s Playback Transfer playback to a new device and determine if it should
     * start playing. Responses:
     * - 204: A completed request will return a 204 NO CONTENT response code, and then issue the
     * command to the player. Due to the asynchronous nature of the issuance of the command, you
     * should use the Get Information About The User’s Current Playback endpoint to check that your
     * issued command was handled correctly by the player. If the device is not found, the request
     * will return 404 NOT FOUND response code. If the user making the request is non-premium, a 403
     * FORBIDDEN response code will be returned.
     * - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API
     * Authorization Guide for details. The access token must have been issued on behalf of a user.
     * The access token must have the user-modify-playback-state scope authorized in order to
     * control playback.
     * @param transferPlaybackBody
     * @return [Call]<[Unit]>
     */
    @PUT("me/player")
    fun transferAUsersPlayback(
        @Header("Authorization") authorization: String? = null,
        @Body transferPlaybackBody: TransferPlaybackBody
    ): Completable
}
