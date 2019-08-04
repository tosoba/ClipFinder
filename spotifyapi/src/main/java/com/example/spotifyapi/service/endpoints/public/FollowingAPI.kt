/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.example.spotifyapi.service.endpoints.public

import com.example.spotifyapi.service.SpotifyAPI
import com.example.spotifyapi.service.SpotifyRestAction
import com.example.spotifyapi.service.http.EndpointBuilder
import com.example.spotifyapi.service.http.SpotifyEndpoint
import com.example.spotifyapi.service.http.encode
import com.example.spotifyapi.service.models.BadRequestException
import com.example.spotifyapi.service.models.PlaylistURI
import com.example.spotifyapi.service.models.UserURI
import com.example.spotifyapi.service.models.serialization.toArray
import com.example.spotifyapi.service.utils.Supplier

/**
 * This endpoint allow you check the playlists that a Spotify user follows.
 */
open class FollowingAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Check to see if one or more Spotify users are following a specified playlist.
     *
     * @param playlistOwner id or uri of the creator of the playlist
     * @param playlist playlist id or uri
     * @param users user ids or uris to check
     *
     * @return List of Booleans representing whether the user follows the playlist. User IDs **not** found will return false
     *
     * @throws [BadRequestException] if the playlist is not found OR any user in the list does not exist
     */
    fun areFollowingPlaylist(
            playlistOwner: String,
            playlist: String,
            vararg users: String
    ): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            val user = UserURI(playlistOwner)
            get(
                    EndpointBuilder("/users/${user.id.encode()}/playlists/${PlaylistURI(playlist).id.encode()}/followers/contains")
                            .with("ids", users.joinToString(",") { UserURI(it).id.encode() }).toString()
            ).toArray<Boolean>(api)
        })
    }

    /**
     * Check to see if a specific Spotify user is following the specified playlist.
     *
     * @param playlistOwner id or uri of the creator of the playlist
     * @param playlist playlist id or uri
     * @param user Spotify user id
     *
     * @return booleans representing whether the user follows the playlist. User IDs **not** found will return false
     *
     * @throws [BadRequestException] if the playlist is not found or if the user does not exist
     */
    fun isFollowingPlaylist(playlistOwner: String, playlist: String, user: String): SpotifyRestAction<Boolean> {
        return toAction(Supplier {
            areFollowingPlaylist(
                    playlistOwner,
                    playlist,
                    users = *arrayOf(user)
            ).complete()[0]
        })
    }
}
