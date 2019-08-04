/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.example.spotifyapi.service.endpoints.client

import com.example.spotifyapi.service.SpotifyAPI
import com.example.spotifyapi.service.SpotifyRestAction
import com.example.spotifyapi.service.SpotifyScope
import com.example.spotifyapi.service.endpoints.public.UserAPI
import com.example.spotifyapi.service.http.EndpointBuilder
import com.example.spotifyapi.service.models.SpotifyUserInformation
import com.example.spotifyapi.service.models.serialization.toObject
import com.example.spotifyapi.service.utils.Supplier

/**
 * Endpoints for retrieving information about a user’s profile.
 */
class ClientUserAPI(api: SpotifyAPI) : UserAPI(api) {
    /**
     * Get detailed profile information about the current user (including the current user’s username).
     *
     * The access token must have been issued on behalf of the current user.
     * Reading the user’s email address requires the [SpotifyScope.USER_READ_EMAIL] scope;
     * reading country and product subscription level requires the [SpotifyScope.USER_READ_PRIVATE] scope.
     * Reading the user’s birthdate requires the [SpotifyScope.USER_READ_BIRTHDATE] scope.
     *
     * @return Never-null [SpotifyUserInformation] object with possibly-null country, email, subscription and birthday fields
     */
    fun getUserProfile(): SpotifyRestAction<SpotifyUserInformation> {
        return toAction(Supplier {
            get(EndpointBuilder("/me").toString()).toObject<SpotifyUserInformation>(api)
        })
    }
}