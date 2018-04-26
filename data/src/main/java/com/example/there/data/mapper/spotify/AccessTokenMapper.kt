package com.example.there.data.mapper.spotify

import com.example.there.data.entities.spotify.AccessTokenData
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.spotify.AccessTokenEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessTokenMapper @Inject constructor() : Mapper<AccessTokenData, AccessTokenEntity>() {
    override fun mapFrom(from: AccessTokenData): AccessTokenEntity = AccessTokenEntity(token = from.accessToken, timestamp = System.currentTimeMillis())
}