package com.example.there.data.mapper

import com.example.there.data.entities.AccessTokenData
import com.example.there.domain.common.Mapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessTokenMapper @Inject constructor() : Mapper<AccessTokenData, String>() {
    override fun mapFrom(from: AccessTokenData): String = from.accessToken
}