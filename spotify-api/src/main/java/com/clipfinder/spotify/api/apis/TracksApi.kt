package com.clipfinder.spotify.api.apis

import com.clipfinder.spotify.api.models.*
import com.example.core.retrofit.NetworkResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TracksApi {
    /**
     * Get Audio Analysis for a Track
     * Get a detailed audio analysis for a single track identified by its unique Spotify ID.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an audio analysis object in JSON format. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details.
     * @param id The Spotify ID for the track.
     * @return [Call]<[AudioAnalysisObject]>
     */
    @GET("audio-analysis/{id}")
    fun endpointGetAudioAnalysis(@Header("Authorization") authorization: String? = null, @Path("id") id: String): Single<NetworkResponse<AudioAnalysisObject, ErrorResponse>>

    /**
     * Get Audio Features for a Track
     * Get audio feature information for a single track identified by its unique Spotify ID.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an audio features object in JSON format. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details.
     * @param id The Spotify ID for the track.
     * @return [Call]<[AudioFeaturesObject]>
     */
    @GET("audio-features/{id}")
    fun endpointGetAudioFeatures(@Header("Authorization") authorization: String? = null, @Path("id") id: String): Single<NetworkResponse<AudioFeaturesObject, ErrorResponse>>

    /**
     * Get Audio Features for Several Tracks
     * Get audio features for multiple tracks based on their Spotify IDs.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an object whose key is \"audio_features\" and whose value is an array of audio features objects in JSON format. Objects are returned in the order requested. If an object is not found, a null value is returned in the appropriate position. Duplicate ids in the query will result in duplicate objects in the response. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details.
     * @param ids A comma-separated list of the Spotify IDs for the tracks. Maximum: 100 IDs.
     * @return [Call]<[AudioFeaturesArrayObject]>
     */
    @GET("audio-features")
    fun endpointGetSeveralAudioFeatures(@Header("Authorization") authorization: String? = null, @Query("ids") ids: String): Single<NetworkResponse<AudioFeaturesArrayObject, ErrorResponse>>

    /**
     * Get Several Tracks
     * Get Spotify catalog information for multiple tracks based on their Spotify IDs.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains an object whose key is tracks and whose value is an array of track objects in JSON format. Objects are returned in the order requested. If an object is not found, a null value is returned in the appropriate position. Duplicate ids in the query will result in duplicate objects in the response. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details.
     * @param ids A comma-separated list of the Spotify IDs for the tracks. Maximum: 50 IDs.
     * @param market An ISO 3166-1 alpha-2 country code or the string from_token. Provide this parameter if you want to apply Track Relinking. (optional)
     * @return [Call]<[TracksObject]>
     */
    @GET("tracks")
    fun endpointGetSeveralTracks(@Header("Authorization") authorization: String? = null, @Query("ids") ids: String, @Query("market") market: String? = null): Single<NetworkResponse<TracksObject, ErrorResponse>>

    /**
     * Get a Track
     * Get Spotify catalog information for a single track identified by its unique Spotify ID.
     * Responses:
     *  - 200: On success, the HTTP status code in the response header is 200 OK and the response body contains a track object in JSON format. On error, the header status code is an error code and the response body contains an error object.
     *  - 0: Unexpected error
     *
     * @param authorization A valid access token from the Spotify Accounts service: see the Web API Authorization Guide for details.
     * @param id The Spotify ID for the track.
     * @param market An ISO 3166-1 alpha-2 country code or the string from_token. Provide this parameter if you want to apply Track Relinking. (optional)
     * @return [Call]<[TrackObject]>
     */
    @GET("tracks/{id}")
    fun endpointGetTrack(@Header("Authorization") authorization: String? = null, @Path("id") id: String, @Query("market") market: String? = null): Single<NetworkResponse<TrackObject, ErrorResponse>>

}
