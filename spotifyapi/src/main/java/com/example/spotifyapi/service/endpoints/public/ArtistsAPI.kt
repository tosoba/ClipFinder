/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.example.spotifyapi.service.endpoints.public

import com.example.spotifyapi.service.SpotifyAPI
import com.example.spotifyapi.service.SpotifyRestAction
import com.example.spotifyapi.service.SpotifyRestActionPaging
import com.example.spotifyapi.service.http.EndpointBuilder
import com.example.spotifyapi.service.http.SpotifyEndpoint
import com.example.spotifyapi.service.http.encode
import com.example.spotifyapi.service.models.*
import com.example.spotifyapi.service.models.serialization.toInnerArray
import com.example.spotifyapi.service.models.serialization.toObject
import com.example.spotifyapi.service.models.serialization.toPagingObject
import com.example.spotifyapi.service.utils.Supplier
import com.neovisionaries.i18n.CountryCode

/**
 * Endpoints for retrieving information about one or more artists from the Spotify catalog.
 */
class ArtistsAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get Spotify catalog information for a single artist identified by their unique Spotify ID.
     *
     * @param artist The spotify id or uri for the artist.
     *
     * @return [Artist] if valid artist id is provided, otherwise null
     */
    fun getArtist(artist: String): SpotifyRestAction<Artist> {
        return toAction(Supplier {
            get(EndpointBuilder("/artists/${ArtistURI(artist).id.encode()}").toString())
                    .toObject<Artist>(api)
        })
    }

    /**
     * Get Spotify catalog information for several artists based on their Spotify IDs. **Artists not found are returned as null inside the ordered list**
     *
     * @param artists The spotify ids or uris representing the artists.
     *
     * @return List of [Artist] objects or null if the artist could not be found, in the order requested
     */
    fun getArtists(vararg artists: String): SpotifyRestAction<List<Artist?>> {
        return toAction(Supplier {
            get(
                    EndpointBuilder("/artists").with(
                            "ids",
                            artists.joinToString(",") { ArtistURI(it).id.encode() }).toString()
            ).toObject<ArtistList>(api).artists
        })
    }

    /**
     * Get Spotify catalog information about an artist’s items.
     *
     * @param artist The artist id or uri
     * @param market Supply this parameter to limit the response to one particular geographical market.
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param include List of keywords that will be used to filter the response. If not supplied, all album groups will be returned.
     *
     * @throws BadRequestException if [artist] is not found, or filter parameters are illegal
     * @return [PagingObject] of [SimpleAlbum] objects
     */
    fun getArtistAlbums(
            artist: String,
            limit: Int? = null,
            offset: Int? = null,
            market: CountryCode? = null,
            vararg include: AlbumInclusionStrategy
    ): SpotifyRestActionPaging<SimpleAlbum, PagingObject<SimpleAlbum>> {
        return toActionPaging(Supplier {
            get(
                    EndpointBuilder("/artists/${ArtistURI(artist).id.encode()}/items").with("limit", limit).with(
                            "offset",
                            offset
                    ).with("market", market?.name)
                            .with("include_groups", include.joinToString(",") { it.keyword }).toString()
            ).toPagingObject<SimpleAlbum>(null, this)
        })
    }

    /**
     * Describes object types to include when finding items
     *
     * @param keyword The spotify id of the strategy
     */
    enum class AlbumInclusionStrategy(val keyword: String) {
        ALBUM("album"), SINGLE("single"), APPEARS_ON("appears_on"), COMPILATION("compilation")
    }

    /**
     * Get Spotify catalog information about an artist’s top tracks **by country**.
     *
     * Contains only up to **10** tracks with *no* [CursorBasedPagingObject] to go between top track pages. Thus, only the top
     * 10 are exposed
     *
     * @param artist The spotify id or uri for the artist.
     * @param market The country ([Market]) to search. Unlike endpoints with optional Track Relinking, the Market is **not** optional.
     *
     * @throws BadRequestException if tracks are not available in the specified [Market] or the [artist] is not found
     * @return List of the top [Track]s of an artist in the given market
     */
    fun getArtistTopTracks(artist: String, market: CountryCode = CountryCode.US): SpotifyRestAction<List<Track>> {
        return toAction(Supplier {
            get(
                    EndpointBuilder("/artists/${ArtistURI(artist).id.encode()}/top-tracks").with(
                            "country",
                            market.name
                    ).toString()
            ).toInnerArray<Track>("tracks")
        })
    }

    /**
     * Get Spotify catalog information about artists similar to a given artist.
     * Similarity is based on analysis of the Spotify community’s listening history.
     *
     * @param artist The spotify id or uri for the artist.
     *
     * @throws BadRequestException if the [artist] is not found
     * @return List of *never-null*, but possibly empty [Artist]s representing similar artists
     */
    fun getRelatedArtists(artist: String): SpotifyRestAction<List<Artist>> {
        return toAction(Supplier {
            get(EndpointBuilder("/artists/${ArtistURI(artist).id.encode()}/related-artists").toString())
                    .toObject<ArtistList>(api).artists.filterNotNull()
        })
    }
}
