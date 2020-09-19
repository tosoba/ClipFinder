package com.clipfinder.spotify.api.apis

import com.clipfinder.spotify.api.models.SearchResponseObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchApi {
    /**
     * Search for an Item
     * Get Spotify Catalog information about albums, artists, playlists, tracks, shows or episodes that match a keyword string.
     * Responses:
     *  - 200: On success: On error:
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details.
     * @param q Search query keywords and optional field filters and operators. For example: q&#x3D;roadhouse%20blues.
     * @param type A comma-separated list of item types to search across. Valid types are: album , artist, playlist, track, show and episode. Search results include hits from all the specified item types. For example: q&#x3D;name:abacab&amp;type&#x3D;album,track returns both albums and tracks with “abacab” included in their name.
     * @param market An ISO 3166-1 alpha-2 country code or the string from_token. If a country code is specified, only content that is playable in that market is returned. Note: - Playlist results are not affected by the market parameter. - If market is set to from_token, and a valid access token is specified in the request header, only content playable in the country associated with the user account, is returned. - Users can view the country that is associated with their account in the account settings. A user must grant access to the user-read-private scope prior to when the access token is issued. (optional)
     * @param limit Maximum number of results to return. Default: 20 Minimum: 1 Maximum: 50 Note: The limit is applied within each type, not on the total response. For example, if the limit value is 3 and the type is artist,album, the response contains 3 artists and 3 albums. (optional)
     * @param offset The index of the first result to return. Default: 0 (the first result). Maximum offset (including limit): 2,000. Use with limit to get the next page of search results. (optional)
     * @param includeExternal Possible values: audio If include_external&#x3D;audio is specified the response will include any relevant audio content that is hosted externally. By default external content is filtered out from responses. (optional)
     * @return [Call]<[SearchResponseObject]>
     */
    @GET("search")
    fun endpointSearch(@Header("Authorization") authorization: String, @Query("q") q: String, @Query("type") type: String, @Query("market") market: String? = null, @Query("limit") limit: Int? = null, @Query("offset") offset: Int? = null, @Query("include_external") includeExternal: String? = null): Call<SearchResponseObject>

}
