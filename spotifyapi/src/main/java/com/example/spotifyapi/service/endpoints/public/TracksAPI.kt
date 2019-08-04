/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.example.spotifyapi.service.endpoints.public

import com.example.spotifyapi.service.SpotifyAPI
import com.example.spotifyapi.service.SpotifyRestAction
import com.example.spotifyapi.service.http.EndpointBuilder
import com.example.spotifyapi.service.http.SpotifyEndpoint
import com.example.spotifyapi.service.http.encode
import com.example.spotifyapi.service.models.*
import com.example.spotifyapi.service.models.serialization.toObject
import com.example.spotifyapi.service.utils.Supplier
import com.neovisionaries.i18n.CountryCode

/**
 * Endpoints for retrieving information about one or more tracks from the Spotify catalog.
 */
class TracksAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get Spotify catalog information for a single track identified by its unique Spotify ID.
     *
     * @param track The spotify id or uri for the track.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @return possibly-null Track. This behavior is *the same* as in [getTracks]
     */
    fun getTrack(track: String, market: CountryCode? = null): SpotifyRestAction<Track> {
        return toAction(Supplier {
            get(EndpointBuilder("/tracks/${TrackURI(track).id.encode()}").with("market", market?.name).toString())
                    .toObject<Track>(api)
        })
    }

    /**
     * Get Spotify catalog information for multiple tracks based on their Spotify IDs.
     *
     * @param tracks The spotify id or uri for the tracks.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @return List of possibly-null full [Track] objects.
     */
    fun getTracks(vararg tracks: String, market: CountryCode? = null): SpotifyRestAction<List<Track?>> {
        return toAction(Supplier {
            get(EndpointBuilder("/tracks").with("ids", tracks.joinToString(",") { TrackURI(it).id.encode() })
                    .with("market", market?.name).toString())
                    .toObject<TrackList>(api).tracks
        })
    }

    /**
     * Get a detailed audio analysis for a single track identified by its unique Spotify ID.
     *
     * The Audio Analysis endpoint provides low-level audio analysis for all of the tracks in the Spotify catalog.
     * The Audio Analysis describes the track’s structure and musical content, including rhythm, pitch, and timbre.
     * All information is precise to the audio sample.
     *
     * Many elements of analysis include confidence values, a floating-point number ranging from 0.0 to 1.0.
     * Confidence indicates the reliability of its corresponding attribute. Elements carrying a small confidence value
     * should be considered speculative. There may not be sufficient data in the audio to compute the attribute with
     * high certainty.
     *
     * @param track The spotify id or uri for the track.
     *
     * @throws BadRequestException if [track] cannot be found
     */
    fun getAudioAnalysis(track: String): SpotifyRestAction<AudioAnalysis> {
        return toAction(Supplier {
            get(EndpointBuilder("/audio-analysis/${TrackURI(track).id.encode()}").toString())
                    .toObject<AudioAnalysis>(api)
        })
    }

    /**
     * Get audio feature information for a single track identified by its unique Spotify ID.
     *
     * @param track The spotify id or uri for the track.
     *
     * @throws BadRequestException if [track] cannot be found
     */
    fun getAudioFeatures(track: String): SpotifyRestAction<AudioFeatures> {
        return toAction(Supplier {
            get(EndpointBuilder("/audio-features/${TrackURI(track).id.encode()}").toString())
                    .toObject<AudioFeatures>(api)
        })
    }

    /**
     * Get audio features for multiple tracks based on their Spotify IDs.
     *
     * @param tracks vararg of spotify track ids or uris.
     *
     * @return Ordered list of possibly-null [AudioFeatures] objects.
     */
    fun getAudioFeatures(vararg tracks: String): SpotifyRestAction<List<AudioFeatures?>> {
        return toAction(Supplier {
            get(EndpointBuilder("/audio-features").with("ids", tracks.joinToString(",") { TrackURI(it).id.encode() }).toString())
                    .toObject<AudioFeaturesResponse>(api).audioFeatures
        })
    }
}
