package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.UserData
import com.example.there.data.util.firstIconUrlOrDefault
import com.example.there.domain.common.OneWayMapper
import com.example.there.domain.entity.spotify.UserEntity

object UserMapper : OneWayMapper<UserData, UserEntity>() {
    override fun mapFrom(from: UserData): UserEntity = UserEntity(
            name = from.name,
            iconUrl = from.icons.firstIconUrlOrDefault
    )
}