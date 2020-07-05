package com.example.spotifyrepo.mapper

import com.example.spotifyapi.model.UserApiModel
import com.example.spotifyrepo.util.firstIconUrlOrDefault
import com.example.there.domain.entity.spotify.UserEntity

val UserApiModel.domain: UserEntity
    get() = UserEntity(
        name = name,
        iconUrl = icons.firstIconUrlOrDefault
    )
