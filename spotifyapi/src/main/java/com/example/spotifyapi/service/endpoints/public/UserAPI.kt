/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.example.spotifyapi.service.endpoints.public

import com.example.spotifyapi.service.SpotifyAPI
import com.example.spotifyapi.service.SpotifyRestAction
import com.example.spotifyapi.service.http.EndpointBuilder
import com.example.spotifyapi.service.http.SpotifyEndpoint
import com.example.spotifyapi.service.http.encode
import com.example.spotifyapi.service.models.SpotifyPublicUser
import com.example.spotifyapi.service.models.UserURI
import com.example.spotifyapi.service.models.serialization.toObject
import com.example.spotifyapi.service.utils.Supplier

/**
 * Endpoints for retrieving information about a user’s profile.
 */
open class UserAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get public profile information about a Spotify user.
     *
     * @param user The user’s Spotify user ID.
     *
     * @return All publicly-available information about the user
     */
    fun getProfile(user: String): SpotifyRestAction<SpotifyPublicUser> {
        return toAction(Supplier {
            get(EndpointBuilder("/users/${UserURI(user).id.encode()}").toString())
                    .toObject<SpotifyPublicUser>(api)
        })
    }
}
