package com.clipfinder.spotify.api.apis

import com.clipfinder.spotify.api.models.*
import com.example.core.retrofit.NetworkResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtistsApi {
    /**
     * Get an Artist
     * Get Spotify catalog information for a single artist identified by their unique Spotify ID.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an artist object in JSON format. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @param id The Spotify ID of the artist.
     * @return [Call]<[ArtistObject]>
     */
    @GET("artists/{id}")
    fun endpointGetAnArtist(@Header("Authorization") authorization: String? = null, @Path("id") id: String): Single<NetworkResponse<ArtistObject, ErrorResponse>>

    /**
     * Get an Artist&#39;s Albums
     * Get Spotify catalog information about an artist’s albums. Optional parameters can be specified in the query string to filter and sort the response.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an array of simplified album objects (wrapped in a paging object) in JSON format. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @param id The Spotify ID for the artist.
     * @param includeGroups A comma-separated list of keywords that will be used to filter the response. If not supplied, all album types will be returned. Valid values are: - album - single - appears_on - compilation For example: include_groups&#x3D;album,single. (optional)
     * @param market Synonym for country. An ISO 3166-1 alpha-2 country code or the string from_token. Supply this parameter to limit the response to one particular geographical market. For example, for albums available in Sweden: market&#x3D;SE. If not given, results will be returned for all markets and you are likely to get duplicate results per album, one for each market in which the album is available! (optional)
     * @param limit The number of album objects to return. Default: 20. Minimum: 1. Maximum: 50. For example: limit&#x3D;2 (optional)
     * @param offset The index of the first album to return. Default: 0 (i.e., the first album). Use with limit to get the next set of albums. (optional)
     * @return [Call]<[SimplifiedAlbumsPagingObject]>
     */
    @GET("artists/{id}/albums")
    fun endpointGetAnArtistsAlbums(@Header("Authorization") authorization: String? = null, @Path("id") id: String, @Query("include_groups") includeGroups: String? = null, @Query("market") market: String? = null, @Query("limit") limit: Int? = null, @Query("offset") offset: Int? = null): Single<NetworkResponse<SimplifiedAlbumsPagingObject, ErrorResponse>>

    /**
     * Get an Artist&#39;s Related Artists
     * Get Spotify catalog information about artists similar to a given artist. Similarity is based on analysis of the Spotify community’s listening history.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an object whose key is \"artists\" and whose value is an array of up to 20 artist objects in JSON format. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @param id The Spotify ID for the artist
     * @return [Call]<[ArtistsObject]>
     */
    @GET("artists/{id}/related-artists")
    fun endpointGetAnArtistsRelatedArtists(@Header("Authorization") authorization: String? = null, @Path("id") id: String): Single<NetworkResponse<ArtistsObject, ErrorResponse>>

    /**
     * Get an Artist&#39;s Top Tracks
     * Get Spotify catalog information about an artist’s top tracks by country.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an object whose key is \"tracks\" and whose value is an array of up to 10 track objects in JSON format. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @param id The Spotify ID for the artist
     * @param market An ISO 3166-1 alpha-2 country code or the string from_token. Synonym for country.
     * @return [Call]<[TracksObject]>
     */
    @GET("artists/{id}/top-tracks")
    fun endpointGetAnArtistsTopTracks(@Header("Authorization") authorization: String? = null, @Path("id") id: String, @Query("market") market: String): Single<NetworkResponse<TracksObject, ErrorResponse>>

    /**
     * Get Multiple Artists
     * Get Spotify catalog information for several artists based on their Spotify IDs.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an object whose key is \"artists\" and whose value is an array of artist objects in JSON format. Objects are returned in the order requested. If an object is not found, a null value is returned in the appropriate position. Duplicate ids in the query will result in duplicate objects in the response. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @param ids A comma-separated list of the Spotify IDs for the artists. Maximum: 50 IDs.
     * @return [Call]<[ArtistsObject]>
     */
    @GET("artists")
    fun endpointGetMultipleArtists(@Header("Authorization") authorization: String? = null, @Query("ids") ids: String): Single<NetworkResponse<ArtistsObject, ErrorResponse>>

}
