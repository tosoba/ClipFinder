package com.example.there.data.mapper

import com.example.there.data.entities.AccessTokenData
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.AccessTokenEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessTokenMapper @Inject constructor() : Mapper<AccessTokenData, AccessTokenEntity>() {
    override fun mapFrom(from: AccessTokenData): AccessTokenEntity = AccessTokenEntity(token = from.accessToken, timestamp = System.currentTimeMillis())
}