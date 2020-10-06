package com.clipfinder.spotify.api.model

import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.squareup.moshi.Json

sealed class TrackOrEpisodeObject(@Json(name = "type") val type: TrackOrEpisodeType)

enum class TrackOrEpisodeType {
    track,
    episode
}

/**
 *
 * @param album
 * @param artists The artists who performed the track. Each artist object includes a link in href to more detailed information about the artist.
 * @param availableMarkets A list of the countries in which the track can be played, identified by their ISO 3166-1 alpha-2 code.
 * @param discNumber The disc number (usually 1 unless the album consists of more than one disc).
 * @param durationMs The track length in milliseconds.
 * @param explicit Whether or not the track has explicit lyrics ( true = yes it does; false = no it does not OR unknown).
 * @param externalIds
 * @param externalUrls
 * @param href A link to the Web API endpoint providing full details of the track.
 * @param id The Spotify ID for the track.
 * @param isPlayable Part of the response when Track Relinking is applied. If true , the track is playable in the given market. Otherwise false.
 * @param linkedFrom
 * @param name The name of the track.
 * @param popularity The popularity of the track. The value will be between 0 and 100, with 100 being the most popular. The popularity of a track is a value between 0 and 100, with 100 being the most popular. The popularity is calculated by algorithm and is based, in the most part, on the total number of plays the track has had and how recent those plays are. Generally speaking, songs that are being played a lot now will have a higher popularity than songs that were played a lot in the past. Duplicate tracks (e.g. the same track from a single and an album) are rated independently. Artist and album popularity is derived mathematically from track popularity. Note that the popularity value may lag actual popularity by a few days: the value is not updated in real time.
 * @param previewUrl A link to a 30 second preview (MP3 format) of the track. Can be null
 * @param restrictions Part of the response when Track Relinking is applied, the original track is not available in the given market, and Spotify did not have any tracks to relink it with. The track response will still contain metadata for the original track, and a restrictions object containing the reason why the track is not available: \"restrictions\" : {\"reason\" : \"market\"}
 * @param trackNumber The number of the track. If an album has several discs, the track number is the number on the specified disc.
 * @param type The object type: “track”.
 * @param uri The Spotify URI for the track.
 */

data class TrackObject(
    @Json(name = "album")
    override val album: SimplifiedAlbumObject,
    /* The artists who performed the track. Each artist object includes a link in href to more detailed information about the artist. */
    @Json(name = "artists")
    override val artists: List<SimplifiedArtistObject>,
    /* A list of the countries in which the track can be played, identified by their ISO 3166-1 alpha-2 code. */
    @Json(name = "available_markets")
    val availableMarkets: List<String>,
    /* The disc number (usually 1 unless the album consists of more than one disc). */
    @Json(name = "disc_number")
    val discNumber: Int,
    /* The track length in milliseconds. */
    @Json(name = "duration_ms")
    override val durationMs: Int,
    /* Whether or not the track has explicit lyrics ( true = yes it does; false = no it does not OR unknown). */
    @Json(name = "explicit")
    val explicit: Boolean,
    @Json(name = "external_ids")
    val externalIds: ExternalIdObject,
    @Json(name = "external_urls")
    val externalUrls: Any,
    /* A link to the Web API endpoint providing full details of the track. */
    @Json(name = "href")
    val href: String,
    /* The Spotify ID for the track. */
    @Json(name = "id")
    override val id: String,
    /* Part of the response when Track Relinking is applied. If true , the track is playable in the given market. Otherwise false. */
    @Json(name = "is_playable")
    val isPlayable: Boolean? = null,
    @Json(name = "linked_from")
    val linkedFrom: LinkedTrackObject? = null,
    /* The name of the track. */
    @Json(name = "name")
    override val name: String,
    /* The popularity of the track. The value will be between 0 and 100, with 100 being the most popular. The popularity of a track is a value between 0 and 100, with 100 being the most popular. The popularity is calculated by algorithm and is based, in the most part, on the total number of plays the track has had and how recent those plays are. Generally speaking, songs that are being played a lot now will have a higher popularity than songs that were played a lot in the past. Duplicate tracks (e.g. the same track from a single and an album) are rated independently. Artist and album popularity is derived mathematically from track popularity. Note that the popularity value may lag actual popularity by a few days: the value is not updated in real time. */
    @Json(name = "popularity")
    override val popularity: Int,
    /* A link to a 30 second preview (MP3 format) of the track. Can be null */
    @Json(name = "preview_url")
    override val previewUrl: String? = null,
    /* Part of the response when Track Relinking is applied, the original track is not available in the given market, and Spotify did not have any tracks to relink it with. The track response will still contain metadata for the original track, and a restrictions object containing the reason why the track is not available: \"restrictions\" : {\"reason\" : \"market\"} */
    @Json(name = "restrictions")
    val restrictions: List<TrackRestrictionObject>? = null,
    /* The number of the track. If an album has several discs, the track number is the number on the specified disc. */
    @Json(name = "track_number")
    override val trackNumber: Int,
    /* The Spotify URI for the track. */
    @Json(name = "uri")
    override val uri: String
) : TrackOrEpisodeObject(TrackOrEpisodeType.track),
    ISpotifyTrack

/**
 *
 * @param audioPreviewUrl A URL to a 30 second preview (MP3 format) of the episode. null if not available.
 * @param description A description of the episode.
 * @param durationMs The episode length in milliseconds.
 * @param explicit Whether or not the episode has explicit content (true = yes it does; false = no it does not OR unknown).
 * @param externalUrls
 * @param href A link to the Web API endpoint providing full details of the episode.
 * @param id The Spotify ID for the episode.
 * @param images The cover art for the episode in various sizes, widest first.
 * @param isExternallyHosted True if the episode is hosted outside of Spotify’s CDN.
 * @param isPlayable True if the episode is playable in the given market. Otherwise false.
 * @param language Note: This field is deprecated and might be removed in the future. Please use the languages field instead. The language used in the episode, identified by a ISO 639 code.
 * @param languages A list of the languages used in the episode, identified by their ISO 639 code.
 * @param name The name of the episode.
 * @param releaseDate The date the episode was first released, for example \"1981-12-15\". Depending on the precision, it might be shown as \"1981\" or \"1981-12\".
 * @param releaseDatePrecision The precision with which release_date value is known: \"year\", \"month\", or \"day\".
 * @param resumePoint
 * @param show
 * @param type The object type: “episode”.
 * @param uri The Spotify URI for the episode.
 */

data class EpisodeObject(
    /* A URL to a 30 second preview (MP3 format) of the episode. null if not available. */
    @Json(name = "audio_preview_url")
    val audioPreviewUrl: String? = null,
    /* A description of the episode. */
    @Json(name = "description")
    val description: String,
    /* The episode length in milliseconds. */
    @Json(name = "duration_ms")
    val durationMs: Int,
    /* Whether or not the episode has explicit content (true = yes it does; false = no it does not OR unknown). */
    @Json(name = "explicit")
    val explicit: Boolean,
    @Json(name = "external_urls")
    val externalUrls: Any,
    /* A link to the Web API endpoint providing full details of the episode. */
    @Json(name = "href")
    val href: String,
    /* The Spotify ID for the episode. */
    @Json(name = "id")
    val id: String,
    /* The cover art for the episode in various sizes, widest first. */
    @Json(name = "images")
    val images: List<ImageObject>,
    /* True if the episode is hosted outside of Spotify’s CDN. */
    @Json(name = "is_externally_hosted")
    val isExternallyHosted: Boolean,
    /* True if the episode is playable in the given market. Otherwise false. */
    @Json(name = "is_playable")
    val isPlayable: Boolean,
    /* Note: This field is deprecated and might be removed in the future. Please use the languages field instead. The language used in the episode, identified by a ISO 639 code. */
    @Json(name = "language")
    val language: String,
    /* A list of the languages used in the episode, identified by their ISO 639 code. */
    @Json(name = "languages")
    val languages: List<String>,
    /* The name of the episode. */
    @Json(name = "name")
    val name: String,
    /* The date the episode was first released, for example \"1981-12-15\". Depending on the precision, it might be shown as \"1981\" or \"1981-12\". */
    @Json(name = "release_date")
    val releaseDate: String,
    /* The precision with which release_date value is known: \"year\", \"month\", or \"day\". */
    @Json(name = "release_date_precision")
    val releaseDatePrecision: String,
    @Json(name = "resume_point")
    val resumePoint: ResumePointObject,
    @Json(name = "show")
    val show: SimplifiedShowObject,
    /* The Spotify URI for the episode. */
    @Json(name = "uri")
    val uri: String
) : TrackOrEpisodeObject(TrackOrEpisodeType.episode)
