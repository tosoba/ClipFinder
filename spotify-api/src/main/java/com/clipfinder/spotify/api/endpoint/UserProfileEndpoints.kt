package com.clipfinder.spotify.api.endpoint

import com.clipfinder.spotify.api.model.ErrorResponse
import com.clipfinder.spotify.api.model.PrivateUserObject
import com.clipfinder.spotify.api.model.PublicUserObject
import com.example.core.model.NetworkResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserProfileEndpoints {
    /**
     * Get Current User&#39;s Profile
     * Get detailed profile information about the current user (including the current user’s username).
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a user object in JSON format. On error, the header status code is an error code and the response body contains an error object. When requesting fields that you don’t have the user’s authorization to access, it will return error 403 Forbidden. Important! If the user-read-email scope is authorized, the returned JSON will include the email address that was entered when the user created their Spotify account. This email address is unverified; do not assume that the email address belongs to the user.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details. The access token must have been issued on behalf of the current user. Reading the user’s email address requires the user-read-email scope; reading country and product subscription level requires the user-read-private scope. See Using Scopes.
     * @return [Call]<[PrivateUserObject]>
     */
    @GET("me")
    fun getCurrentUsersProfile(@Header("Authorization") authorization: String? = null): Single<NetworkResponse<PrivateUserObject, ErrorResponse>>

    /**
     * Get a User&#39;s Profile
     * Get public profile information about a Spotify user.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a user object in JSON format. On error, the header status code is an error code and the response body contains an error object. If a user with that user_id doesn’t exist, the status code is 404 NOT FOUND.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details.
     * @param userId The user’s Spotify user ID.
     * @return [Call]<[PublicUserObject]>
     */
    @GET("users/{user_id}")
    fun getUsersProfile(@Header("Authorization") authorization: String? = null, @Path("user_id") userId: String): Single<NetworkResponse<PublicUserObject, ErrorResponse>>
}
