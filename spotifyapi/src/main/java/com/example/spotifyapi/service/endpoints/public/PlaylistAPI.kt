/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.example.spotifyapi.service.endpoints.public

import com.example.spotifyapi.service.SpotifyAPI
import com.example.spotifyapi.service.SpotifyRestAction
import com.example.spotifyapi.service.SpotifyRestActionPaging
import com.example.spotifyapi.service.SpotifyScope
import com.example.spotifyapi.service.http.EndpointBuilder
import com.example.spotifyapi.service.http.SpotifyEndpoint
import com.example.spotifyapi.service.http.encode
import com.example.spotifyapi.service.models.*
import com.example.spotifyapi.service.models.serialization.toArray
import com.example.spotifyapi.service.models.serialization.toObject
import com.example.spotifyapi.service.models.serialization.toPagingObject
import com.example.spotifyapi.service.utils.Supplier
import com.neovisionaries.i18n.CountryCode

/**
 * Endpoints for retrieving information about a user’s playlists
 */
open class PlaylistAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get a list of the playlists owned or followed by a Spotify user. Lookups for non-existant users return an empty
     * [PagingObject] (blame Spotify)
     *
     * **Note that** private playlists are only retrievable for the current user and require the [SpotifyScope.PLAYLIST_READ_PRIVATE] scope
     * to have been authorized by the user. Note that this scope alone will not return a collaborative playlist, even
     * though they are always private.
     * Collaborative playlists are only retrievable for the current user and require the [SpotifyScope.PLAYLIST_READ_COLLABORATIVE]
     * scope to have been authorized by the user.
     *
     * @param user The user’s Spotify user ID.
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @return [PagingObject] of [SimplePlaylist]s **ONLY if** the user can be found. Otherwise, an empty paging object is returned.
     * This does not have the detail of full [Playlist] objects.
     *
     * @throws BadRequestException if the user is not found (404)
     *
     */
    fun getPlaylists(
            user: String,
            limit: Int? = null,
            offset: Int? = null
    ): SpotifyRestActionPaging<SimplePlaylist, PagingObject<SimplePlaylist>> {
        return toActionPaging(Supplier {
            get(
                    EndpointBuilder("/users/${UserURI(user).id.encode()}/playlists").with("limit", limit).with(
                            "offset", offset
                    ).toString()
            ).toPagingObject<SimplePlaylist>(endpoint = this)
        })
    }

    /**
     * Get a playlist owned by a Spotify user.
     *
     * **Note that** both Public and Private playlists belonging to any user are retrievable on provision of a valid access token.
     *
     * @param playlist The spotify id or uri for the playlist.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @throws BadRequestException if the playlist is not found
     */
    fun getPlaylist(playlist: String, market: CountryCode? = null): SpotifyRestAction<Playlist> {
        return toAction(Supplier {
            get(
                    EndpointBuilder("/playlists/${PlaylistURI(playlist).id.encode()}")
                            .with("market", market?.name).toString()
            ).toObject<Playlist>(api)
        })
    }

    /**
     * Get full details of the tracks of a playlist owned by a Spotify user.
     *
     * **Note that** both Public and Private playlists belonging to any user are retrievable on provision of a valid access token.
     *
     * @param playlist The spotify id or uri for the playlist.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @throws BadRequestException if the playlist cannot be found
     */
    fun getPlaylistTracks(
            playlist: String,
            limit: Int? = null,
            offset: Int? = null,
            market: CountryCode? = null
    ): SpotifyRestActionPaging<PlaylistTrack, PagingObject<PlaylistTrack>> {
        return toActionPaging(Supplier {
            get(
                    EndpointBuilder("/playlists/${PlaylistURI(playlist).id.encode()}/tracks").with("limit", limit)
                            .with("offset", offset).with("market", market?.name).toString()
            )
                    .toPagingObject<PlaylistTrack>(null, this)
        })
    }

    /**
     * Get the current image(s) associated with a specific playlist.
     *
     * This access token must be issued on behalf of the user. Current playlist image for both Public and Private
     * playlists of any user are retrievable on provision of a valid access token.
     *
     * @param playlist The spotify id or uri for the playlist.
     *
     * @throws BadRequestException if the playlist cannot be found
     */
    fun getPlaylistCovers(playlist: String): SpotifyRestAction<List<SpotifyImage>> {
        return toAction(Supplier {
            get(EndpointBuilder("/playlists/${PlaylistURI(playlist).id.encode()}/images").toString())
                    .toArray<SpotifyImage>(api).toList()
        })
    }
}
