/**
 * Spotify Web API No description provided (generated by Openapi Generator
 * https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 0.0.1
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech Do not edit the class manually.
 */
package com.clipfinder.spotify.api.model

import com.clipfinder.core.spotify.model.ISpotifyAudioFeatures
import com.squareup.moshi.Json

/**
 *
 * @param acousticness A confidence measure from 0.0 to 1.0 of whether the track is acoustic. 1.0
 * represents high confidence the track is acoustic.
 * @param analysisUrl An HTTP URL to access the full audio analysis of this track. An access token
 * is required to access this data.
 * @param danceability Danceability describes how suitable a track is for dancing based on a
 * combination of musical elements including tempo, rhythm stability, beat strength, and overall
 * regularity. A value of 0.0 is least danceable and 1.0 is most danceable.
 * @param durationMs The duration of the track in milliseconds.
 * @param energy Energy is a measure from 0.0 to 1.0 and represents a perceptual measure of
 * intensity and activity. Typically, energetic tracks feel fast, loud, and noisy. For example,
 * death metal has high energy, while a Bach prelude scores low on the scale. Perceptual features
 * contributing to this attribute include dynamic range, perceived loudness, timbre, onset rate, and
 * general entropy.
 * @param id The Spotify ID for the track.
 * @param instrumentalness Predicts whether a track contains no vocals. “Ooh” and “aah” sounds are
 * treated as instrumental in this context. Rap or spoken word tracks are clearly “vocal”. The
 * closer the instrumentalness value is to 1.0, the greater likelihood the track contains no vocal
 * content. Values above 0.5 are intended to represent instrumental tracks, but confidence is higher
 * as the value approaches 1.0.
 * @param key The key the track is in. Integers map to pitches using standard Pitch Class notation .
 * E.g. 0 = C, 1 = C♯/D♭, 2 = D, and so on.
 * @param liveness Detects the presence of an audience in the recording. Higher liveness values
 * represent an increased probability that the track was performed live. A value above 0.8 provides
 * strong likelihood that the track is live.
 * @param loudness The overall loudness of a track in decibels (dB). Loudness values are averaged
 * across the entire track and are useful for comparing relative loudness of tracks. Loudness is the
 * quality of a sound that is the primary psychological correlate of physical strength (amplitude).
 * Values typical range between -60 and 0 db.
 * @param mode Mode indicates the modality (major or minor) of a track, the type of scale from which
 * its melodic content is derived. Major is represented by 1 and minor is 0.
 * @param speechiness Speechiness detects the presence of spoken words in a track. The more
 * exclusively speech-like the recording (e.g. talk show, audio book, poetry), the closer to 1.0 the
 * attribute value. Values above 0.66 describe tracks that are probably made entirely of spoken
 * words. Values between 0.33 and 0.66 describe tracks that may contain both music and speech,
 * either in sections or layered, including such cases as rap music. Values below 0.33 most likely
 * represent music and other non-speech-like tracks.
 * @param tempo The overall estimated tempo of a track in beats per minute (BPM). In musical
 * terminology, tempo is the speed or pace of a given piece and derives directly from the average
 * beat duration.
 * @param timeSignature An estimated overall time signature of a track. The time signature (meter)
 * is a notational convention to specify how many beats are in each bar (or measure).
 * @param trackHref A link to the Web API endpoint providing full details of the track.
 * @param type The object type: “audio_features”
 * @param uri The Spotify URI for the track.
 * @param valence A measure from 0.0 to 1.0 describing the musical positiveness conveyed by a track.
 * Tracks with high valence sound more positive (e.g. happy, cheerful, euphoric), while tracks with
 * low valence sound more negative (e.g. sad, depressed, angry).
 */
data class AudioFeaturesObject(
    /* A confidence measure from 0.0 to 1.0 of whether the track is acoustic. 1.0 represents high confidence the track is acoustic. */
    @Json(name = "acousticness") override val acousticness: java.math.BigDecimal,
    /* An HTTP URL to access the full audio analysis of this track. An access token is required to access this data. */
    @Json(name = "analysis_url") val analysisUrl: String,
    /* Danceability describes how suitable a track is for dancing based on a combination of musical elements including tempo, rhythm stability, beat strength, and overall regularity. A value of 0.0 is least danceable and 1.0 is most danceable. */
    @Json(name = "danceability") override val danceability: java.math.BigDecimal,
    /* The duration of the track in milliseconds. */
    @Json(name = "duration_ms") val durationMs: Int,
    /* Energy is a measure from 0.0 to 1.0 and represents a perceptual measure of intensity and activity. Typically, energetic tracks feel fast, loud, and noisy. For example, death metal has high energy, while a Bach prelude scores low on the scale. Perceptual features contributing to this attribute include dynamic range, perceived loudness, timbre, onset rate, and general entropy. */
    @Json(name = "energy") override val energy: java.math.BigDecimal,
    /* The Spotify ID for the track. */
    @Json(name = "id") val id: String,
    /* Predicts whether a track contains no vocals. “Ooh” and “aah” sounds are treated as instrumental in this context. Rap or spoken word tracks are clearly “vocal”. The closer the instrumentalness value is to 1.0, the greater likelihood the track contains no vocal content. Values above 0.5 are intended to represent instrumental tracks, but confidence is higher as the value approaches 1.0. */
    @Json(name = "instrumentalness") override val instrumentalness: java.math.BigDecimal,
    /* The key the track is in. Integers map to pitches using standard Pitch Class notation . E.g. 0 = C, 1 = C♯/D♭, 2 = D, and so on. */
    @Json(name = "key") val key: Int,
    /* Detects the presence of an audience in the recording. Higher liveness values represent an increased probability that the track was performed live. A value above 0.8 provides strong likelihood that the track is live. */
    @Json(name = "liveness") override val liveness: java.math.BigDecimal,
    /* The overall loudness of a track in decibels (dB). Loudness values are averaged across the entire track and are useful for comparing relative loudness of tracks. Loudness is the quality of a sound that is the primary psychological correlate of physical strength (amplitude). Values typical range between -60 and 0 db. */
    @Json(name = "loudness") override val loudness: java.math.BigDecimal,
    /* Mode indicates the modality (major or minor) of a track, the type of scale from which its melodic content is derived. Major is represented by 1 and minor is 0. */
    @Json(name = "mode") val mode: Int,
    /* Speechiness detects the presence of spoken words in a track. The more exclusively speech-like the recording (e.g. talk show, audio book, poetry), the closer to 1.0 the attribute value. Values above 0.66 describe tracks that are probably made entirely of spoken words. Values between 0.33 and 0.66 describe tracks that may contain both music and speech, either in sections or layered, including such cases as rap music. Values below 0.33 most likely represent music and other non-speech-like tracks. */
    @Json(name = "speechiness") override val speechiness: java.math.BigDecimal,
    /* The overall estimated tempo of a track in beats per minute (BPM). In musical terminology, tempo is the speed or pace of a given piece and derives directly from the average beat duration. */
    @Json(name = "tempo") override val tempo: java.math.BigDecimal,
    /* An estimated overall time signature of a track. The time signature (meter) is a notational convention to specify how many beats are in each bar (or measure). */
    @Json(name = "time_signature") val timeSignature: Int,
    /* A link to the Web API endpoint providing full details of the track. */
    @Json(name = "track_href") val trackHref: String,
    /* The object type: “audio_features” */
    @Json(name = "type") val type: String,
    /* The Spotify URI for the track. */
    @Json(name = "uri") val uri: String,
    /* A measure from 0.0 to 1.0 describing the musical positiveness conveyed by a track. Tracks with high valence sound more positive (e.g. happy, cheerful, euphoric), while tracks with low valence sound more negative (e.g. sad, depressed, angry). */
    @Json(name = "valence") override val valence: java.math.BigDecimal
) : ISpotifyAudioFeatures
