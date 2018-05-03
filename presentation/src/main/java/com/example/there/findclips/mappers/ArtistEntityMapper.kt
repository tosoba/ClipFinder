package com.example.there.findclips.mappers

import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.spotify.ArtistEntity
import com.example.there.findclips.entities.Artist

object ArtistEntityMapper : Mapper<ArtistEntity, Artist>() {
    override fun mapFrom(from: ArtistEntity): Artist = Artist(
            id = from.id,
            name = from.name,
            popularity = from.popularity,
            iconUrl = from.iconUrl
    )
}