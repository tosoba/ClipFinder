package com.clipfinder.spotify.api.apis

import com.clipfinder.spotify.api.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface BrowseApi {
    /**
     * Get a Category&#39;s Playlists
     * Get a list of Spotify playlists tagged with a particular category.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an array of simplified playlist objects (wrapped in a paging object) in JSON format. On error, the header status code is an error code and the response body contains an error object. Once you have retrieved the list, you can use Get a Playlist and Get a Playlist’s Tracks to drill down further.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @param categoryId The Spotify category ID for the category.
     * @param country A country: an ISO 3166-1 alpha-2 country code. Provide this parameter to ensure that the category exists for a particular country. (optional)
     * @param limit The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50. (optional)
     * @param offset The index of the first item to return. Default: 0 (the first object). Use with limit to get the next set of items. (optional)
     * @return [Call]<[PlaylistPagingObject]>
     */
    @GET("browse/categories/{category_id}/playlists")
    fun endpointGetACategoriesPlaylists(@Header("Authorization") authorization: String, @Path("category_id") categoryId: String, @Query("country") country: String? = null, @Query("limit") limit: Int? = null, @Query("offset") offset: Int? = null): Call<PlaylistPagingObject>

    /**
     * Get a Category
     * Get a single category used to tag items in Spotify (on, for example, the Spotify player’s “Browse” tab).
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a category object in JSON format. On error, the header status code is an error code and the response body contains an error object. Once you have retrieved the category, you can use Get a Category’s Playlists to drill down further.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @param categoryId The Spotify category ID for the category.
     * @param country A country: an ISO 3166-1 alpha-2 country code. Provide this parameter to ensure that the category exists for a particular country. (optional)
     * @param locale The desired language, consisting of an ISO 639-1 language code and an ISO 3166-1 alpha-2 country code, joined by an underscore. For example: es_MX, meaning \&quot;Spanish (Mexico)\&quot;. Provide this parameter if you want the category strings returned in a particular language. Note that, if locale is not supplied, or if the specified language is not available, the category strings returned will be in the Spotify default language (American English). (optional)
     * @return [Call]<[CategoryObject]>
     */
    @GET("browse/categories/{category_id}")
    fun endpointGetACategory(@Header("Authorization") authorization: String, @Path("category_id") categoryId: String, @Query("country") country: String? = null, @Query("locale") locale: String? = null): Call<CategoryObject>

    /**
     * Get All Categories
     * Get a list of categories used to tag items in Spotify (on, for example, the Spotify player’s “Browse” tab).
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an object with a categories field, with an array of category objects (wrapped in a paging object) in JSON format. On error, the header status code is an error code and the response body contains an error object. Once you have retrieved the list, you can use Get a Category to drill down further.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @param country A country: an ISO 3166-1 alpha-2 country code. Provide this parameter if you want to narrow the list of returned categories to those relevant to a particular country. If omitted, the returned items will be globally relevant. (optional)
     * @param locale The desired language, consisting of an ISO 639-1 language code and an ISO 3166-1 alpha-2 country code, joined by an underscore. For example: es_MX, meaning “Spanish (Mexico)”. Provide this parameter if you want the category metadata returned in a particular language. Note that, if locale is not supplied, or if the specified language is not available, all strings will be returned in the Spotify default language (American English). The locale parameter, combined with the country parameter, may give odd results if not carefully matched. For example country&#x3D;SE&amp;locale&#x3D;de_DE will return a list of categories relevant to Sweden but as German language strings. (optional)
     * @param limit The maximum number of categories to return. Default: 20. Minimum: 1. Maximum: 50. (optional)
     * @param offset The index of the first item to return. Default: 0 (the first object). Use with limit to get the next set of categories. (optional)
     * @return [Call]<[CategoriesObject]>
     */
    @GET("browse/categories")
    fun endpointGetCategories(@Header("Authorization") authorization: String, @Query("country") country: String? = null, @Query("locale") locale: String? = null, @Query("limit") limit: Int? = null, @Query("offset") offset: Int? = null): Call<CategoriesObject>

    /**
     * Get All Featured Playlists
     * Get a list of Spotify featured playlists (shown, for example, on a Spotify player’s ‘Browse’ tab).
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a message and a playlists object. The playlists object contains an array of simplified playlist objects (wrapped in a paging object) in JSON format. On error, the header status code is an error code and the response body contains an error object. Once you have retrieved the list of playlist objects, you can use Get a Playlist and Get a Playlist’s Tracks to drill down further.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @param country A country: an ISO 3166-1 alpha-2 country code. Provide this parameter if you want the list of returned items to be relevant to a particular country. If omitted, the returned items will be relevant to all countries. (optional)
     * @param locale The desired language, consisting of a lowercase ISO 639-1 language code and an uppercase ISO 3166-1 alpha-2 country code, joined by an underscore. For example: es_MX, meaning “Spanish (Mexico)”. Provide this parameter if you want the results returned in a particular language (where available). Note that, if locale is not supplied, or if the specified language is not available, all strings will be returned in the Spotify default language (American English). The locale parameter, combined with the country parameter, may give odd results if not carefully matched. For example country&#x3D;SE&amp;locale&#x3D;de_DE will return a list of categories relevant to Sweden but as German language strings. (optional)
     * @param timestamp A timestamp in ISO 8601 format: yyyy-MM-ddTHH:mm:ss. Use this parameter to specify the user’s local time to get results tailored for that specific date and time in the day. If not provided, the response defaults to the current UTC time. Example: “2014-10-23T09:00:00” for a user whose local time is 9AM. If there were no featured playlists (or there is no data) at the specified time, the response will revert to the current UTC time. (optional)
     * @param limit The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50. (optional)
     * @param offset The index of the first item to return. Default: 0 (the first object). Use with limit to get the next set of items. (optional)
     * @return [Call]<[FeaturedPlaylistObject]>
     */
    @GET("browse/featured-playlists")
    fun endpointGetFeaturedPlaylists(@Header("Authorization") authorization: String, @Query("country") country: String? = null, @Query("locale") locale: String? = null, @Query("timestamp") timestamp: Int? = null, @Query("limit") limit: Int? = null, @Query("offset") offset: Int? = null): Call<FeaturedPlaylistObject>

    /**
     * Get All New Releases
     * Get a list of new album releases featured in Spotify (shown, for example, on a Spotify player’s “Browse” tab).
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a message and analbums object. The albums object contains an array of simplified album objects (wrapped in a paging object) in JSON format. On error, the header status code is an error code and the response body contains an error object. Once you have retrieved the list, you can use Get an Album’s Tracks to drill down further. The results are returned in an order reflected within the Spotify clients, and therefore may not be ordered by date.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @param country A country: an ISO 3166-1 alpha-2 country code. Provide this parameter if you want the list of returned items to be relevant to a particular country. If omitted, the returned items will be relevant to all countries. (optional)
     * @param limit The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50. (optional)
     * @param offset The index of the first item to return. Default: 0 (the first object). Use with limit to get the next set of items. (optional)
     * @return [Call]<[NewReleasesObject]>
     */
    @GET("browse/new-releases")
    fun endpointGetNewReleases(@Header("Authorization") authorization: String, @Query("country") country: String? = null, @Query("limit") limit: Int? = null, @Query("offset") offset: Int? = null): Call<NewReleasesObject>

    /**
     * Get Recommendation Genres
     * Retrieve a list of available genres seed parameter values for recommendations.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a recommendations response object in JSON format.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @return [Call]<[GenreSeedsObject]>
     */
    @GET("recommendations/available-genre-seeds")
    fun endpointGetRecommendationGenres(@Header("Authorization") authorization: String): Call<GenreSeedsObject>

    /**
     * Get Recommendations
     * Recommendations are generated based on the available information for a given seed entity and matched against similar artists and tracks. If there is sufficient information about the provided seeds, a list of tracks will be returned together with pool size details.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a recommendations response object in JSON format.
     *  - 0: Unexpected error
     *
     * @param authorization A valid user access token or your client credentials.
     * @param seedArtists A comma separated list of Spotify IDs for seed artists. Up to 5 seed values may be provided in any combination of seed_artists, seed_tracks and seed_genres.
     * @param seedGenres A comma separated list of any genres in the set of available genre seeds. Up to 5 seed values may be provided in any combination of seed_artists, seed_tracks and seed_genres.
     * @param seedTracks A comma separated list of Spotify IDs for a seed track. Up to 5 seed values may be provided in any combination of seed_artists, seed_tracks and seed_genres.
     * @param limit The target size of the list of recommended tracks. For seeds with unusually small pools or when highly restrictive filtering is applied, it may be impossible to generate the requested number of recommended tracks. Debugging information for such cases is available in the response. Default: 20. Minimum: 1. Maximum: 100. (optional)
     * @param market An ISO 3166-1 alpha-2 country code or the string from_token. Provide this parameter if you want to apply Track Relinking. Because min_*, max_* and target_* are applied to pools before relinking, the generated results may not precisely match the filters applied. Original, non-relinked tracks are available via the linked_from attribute of the relinked track response. (optional)
     * @param minStar Multiple values. For each tunable track attribute, a hard floor on the selected track attribute’s value can be provided. See tunable track attributes below for the list of available options. For example, min_tempo&#x3D;140 would restrict results to only those tracks with a tempo of greater than 140 beats per minute. (optional)
     * @param maxStar Multiple values. For each tunable track attribute, a hard ceiling on the selected track attribute’s value can be provided. See tunable track attributes below for the list of available options. For example, max_instrumentalness&#x3D;0.35 would filter out most tracks that are likely to be instrumental. (optional)
     * @param targetStar Multiple values. For each of the tunable track attributes (below) a target value may be provided. Tracks with the attribute values nearest to the target values will be preferred. For example, you might request target_energy&#x3D;0.6 and target_danceability&#x3D;0.8. All target values will be weighed equally in ranking results. (optional)
     * @return [Call]<[RecommendationsResponseObject]>
     */
    @GET("recommendations")
    fun endpointGetRecommendations(@Header("Authorization") authorization: String, @Query("seed_artists") seedArtists: String, @Query("seed_genres") seedGenres: String, @Query("seed_tracks") seedTracks: String, @Query("limit") limit: Int? = null, @Query("market") market: String? = null, @Query("min_*") minStar: java.math.BigDecimal? = null, @Query("max_*") maxStar: java.math.BigDecimal? = null, @Query("target_*") targetStar: java.math.BigDecimal? = null): Call<RecommendationsResponseObject>

}
