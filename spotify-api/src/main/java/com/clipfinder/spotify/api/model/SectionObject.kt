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

import com.squareup.moshi.Json

/**
 *
 * @param start The starting point (in seconds) of the section.
 * @param duration The duration (in seconds) of the section.
 * @param confidence The confidence, from 0.0 to 1.0, of the reliability of the section’s
 * \"designation\".
 * @param loudness The overall loudness of the section in decibels (dB). Loudness values are useful
 * for comparing relative loudness of sections within tracks.
 * @param tempo The overall estimated tempo of the section in beats per minute (BPM). In musical
 * terminology, tempo is the speed or pace of a given piece and derives directly from the average
 * beat duration.
 * @param tempoConfidence
 * @param key
 * @param keyConfidence
 * @param mode
 * @param modeConfidence
 * @param timeSignature
 * @param timeSignatureConfidence
 */
data class SectionObject(
    /* The starting point (in seconds) of the section. */
    @Json(name = "start") val start: java.math.BigDecimal? = null,
    /* The duration (in seconds) of the section. */
    @Json(name = "duration") val duration: java.math.BigDecimal? = null,
    /* The confidence, from 0.0 to 1.0, of the reliability of the section’s \"designation\". */
    @Json(name = "confidence") val confidence: java.math.BigDecimal? = null,
    /* The overall loudness of the section in decibels (dB). Loudness values are useful for comparing relative loudness of sections within tracks. */
    @Json(name = "loudness") val loudness: java.math.BigDecimal? = null,
    /* The overall estimated tempo of the section in beats per minute (BPM). In musical terminology, tempo is the speed or pace of a given piece and derives directly from the average beat duration. */
    @Json(name = "tempo") val tempo: java.math.BigDecimal? = null,
    @Json(name = "tempo_confidence") val tempoConfidence: java.math.BigDecimal? = null,
    @Json(name = "key") val key: Int? = null,
    @Json(name = "key_confidence") val keyConfidence: java.math.BigDecimal? = null,
    @Json(name = "mode") val mode: Int? = null,
    @Json(name = "mode_confidence") val modeConfidence: java.math.BigDecimal? = null,
    @Json(name = "time_signature") val timeSignature: Int? = null,
    @Json(name = "time_signature_confidence")
    val timeSignatureConfidence: java.math.BigDecimal? = null
)
