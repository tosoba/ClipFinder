package com.example.spotifyapi

import com.example.core.SpotifyDefaults
import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.models.*
import io.reactivex.Single
import retrofit2.http.*

interface SpotifyBrowseApi {

    @GET("browse/categories/{category_id}/playlists")
    fun getPlaylistsForCategory(
            @Header("Authorization") authorization: String,
            @Path("category_id") categoryId: String,
            @Query("offset") offset: Int,
            @Query("country") country: String = SpotifyDefaults.COUNTRY,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT
    ): Single<NetworkResponse<SimplePlaylistsPagedResponse, ErrorResponse>>

    @GET("browse/categories")
    fun getCategories(
            @Header("Authorization") authorization: String,
            @Query("country") country: String = SpotifyDefaults.COUNTRY,
            @Query("locale") locale: String = SpotifyDefaults.LOCALE,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT
    ): Single<NetworkResponse<CategoriesResponse, ErrorResponse>>

    @GET("browse/categories/{category_id}")
    fun getCategory(
            @Header("Authorization") authorization: String,
            @Query("country") country: String = SpotifyDefaults.COUNTRY,
            @Query("locale") locale: String = SpotifyDefaults.LOCALE
    ): Single<NetworkResponse<SpotifyCategory, ErrorResponse>>

    @GET("browse/featured-playlists")
    fun getFeaturedPlaylists(
            @Header("Authorization") authorization: String,
            @Query("country") country: String = SpotifyDefaults.COUNTRY,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("locale") locale: String = SpotifyDefaults.LOCALE
    ): Single<NetworkResponse<FeaturedPlaylists, ErrorResponse>>

    @GET("browse/new-releases")
    fun getNewReleases(
            @Header("Authorization") authorization: String,
            @Query("country") country: String = SpotifyDefaults.COUNTRY,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<SimpleAlbumsPagedResponse, ErrorResponse>>

    @GET("recommendations")
    fun getRecommendations(
            @Query("seed_artists") seedArtists: String? = null,
            @Query("seed_genres") seedGenres: String? = null,
            @Query("seed_tracks") seedTracks: String? = null,
            @Query("market") market: String = SpotifyDefaults.COUNTRY,
            @Query("limit") limit: Int = SpotifyDefaults.TRACKS_LIMIT,
            @QueryMap options: Map<String, String> = emptyMap()
    ): Single<NetworkResponse<RecommendationResponse, ErrorResponse>>

    companion object {
        fun recommendationOptionsWith(
                targetAttributes: Map<TuneableTrackAttribute, Number> = emptyMap(),
                minAttributes: Map<TuneableTrackAttribute, Number> = emptyMap(),
                maxAttributes: Map<TuneableTrackAttribute, Number> = emptyMap()
        ): Map<String, String> {
            fun Map<TuneableTrackAttribute, Number>.toStringPair(
                    prefix: String
            ) = map { (attr, num) -> "${prefix}_${attr.name}" to num.toString() }

            val attributeValues: List<Pair<String, String>> =
                    targetAttributes.toStringPair("target") + minAttributes.toStringPair("min") + maxAttributes.toStringPair("max")
            return mapOf(*attributeValues.toTypedArray())
        }
    }
}
