package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.UserData
import com.example.there.data.util.firstIconUrlOrDefault
import com.example.there.domain.entity.spotify.UserEntity


val UserData.domain: UserEntity
    get() =  UserEntity(
            name = name,
            iconUrl = icons.firstIconUrlOrDefault
    )