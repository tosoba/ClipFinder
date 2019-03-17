package com.example.there.data.mapper.spotify

import com.example.spotifyapi.model.UserApiModel
import com.example.there.data.util.firstIconUrlOrDefault
import com.example.there.domain.entity.spotify.UserEntity


val UserApiModel.domain: UserEntity
    get() = UserEntity(
            name = name,
            iconUrl = icons.firstIconUrlOrDefault
    )