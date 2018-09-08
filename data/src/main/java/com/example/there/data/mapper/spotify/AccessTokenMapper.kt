package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.AccessTokenData
import com.example.there.domain.common.OneWayMapper
import com.example.there.domain.entity.spotify.AccessTokenEntity


object AccessTokenMapper : OneWayMapper<AccessTokenData, AccessTokenEntity>() {
    override fun mapFrom(from: AccessTokenData): AccessTokenEntity = AccessTokenEntity(token = from.accessToken, timestamp = System.currentTimeMillis())
}