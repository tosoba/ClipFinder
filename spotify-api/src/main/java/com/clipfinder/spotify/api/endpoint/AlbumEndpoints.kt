package com.clipfinder.spotify.api.endpoint

import com.clipfinder.core.model.NetworkResponse
import com.clipfinder.spotify.api.model.AlbumObject
import com.clipfinder.spotify.api.model.AlbumsObject
import com.clipfinder.spotify.api.model.ErrorResponse
import com.clipfinder.spotify.api.model.TracksPagingObject
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumEndpoints {
    /**
     * Get an Album
     * Get Spotify catalog information for a single album.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an album object in JSON format. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @param id The Spotify ID of the album.
     * @param market The market you’d like to request. Synonym for country. (optional)
     * @return [Call]<[AlbumObject]>
     */
    @GET("albums/{id}")
    fun getAnAlbum(@Header("Authorization") authorization: String? = null, @Path("id") id: String, @Query("market") market: String? = null): Single<NetworkResponse<AlbumObject, ErrorResponse>>

    /**
     * Get an Album&#39;s Tracks
     * Get Spotify catalog information about an album’s tracks. Optional parameters can be used to limit the number of tracks returned.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an album object in JSON format. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @param id The Spotify ID of the album.
     * @param limit The maximum number of tracks to return. Default: 20. Minimum: 1. Maximum: 50. (optional)
     * @param offset The index of the first track to return. Default: 0 (the first object). Use with limit to get the next set of tracks. (optional)
     * @param market An ISO 3166-1 alpha-2 country code or the string from_token. Provide this parameter if you want to apply Track Relinking. (optional)
     * @return [Call]<[TracksPagingObject]>
     */
    @GET("albums/{id}/tracks")
    fun getAnAlbumsTracks(@Header("Authorization") authorization: String? = null, @Path("id") id: String, @Query("limit") limit: Int? = null, @Query("offset") offset: Int? = null, @Query("market") market: String? = null): Single<NetworkResponse<TracksPagingObject, ErrorResponse>>

    /**
     * Get Multiple Albums
     * Get Spotify catalog information for multiple albums identified by their Spotify IDs.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an object whose key is \"albums\" and whose value is an array of album objects in JSON format. Objects are returned in the order requested. If an object is not found, a null value is returned in the appropriate position. Duplicate ids in the query will result in duplicate objects in the response. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @param ids A comma-separated list of the Spotify IDs for the albums. Maximum: 20 IDs.
     * @param market An ISO 3166-1 alpha-2 country code or the string from_token. Provide this parameter if you want to apply Track Relinking. (optional)
     * @return [Call]<[AlbumsObject]>
     */
    @GET("albums")
    fun getMultipleAlbums(@Header("Authorization") authorization: String? = null, @Query("ids") ids: String, @Query("market") market: String? = null): Single<NetworkResponse<AlbumsObject, ErrorResponse>>
}
