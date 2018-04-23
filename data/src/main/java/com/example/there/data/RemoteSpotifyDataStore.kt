package com.example.there.data

import android.util.Base64
import com.example.there.data.api.SpotifyAccountsApi
import com.example.there.data.api.SpotifyApi
import com.example.there.data.entities.AccessTokenData
import com.example.there.data.entities.CategoryData
import com.example.there.domain.common.Mapper
import com.example.there.domain.SpotifyDataStore
import com.example.there.domain.entities.CategoryEntity
import io.reactivex.Observable

class RemoteSpotifyDataStore(private val api: SpotifyApi,
                             private val accountsApi: SpotifyAccountsApi,
                             private val categoryMapper: Mapper<CategoryData, CategoryEntity>,
                             private val accessTokenMapper: Mapper<AccessTokenData, String>) : SpotifyDataStore {

    override fun getAccessToken(clientId: String, clientSecret: String): Observable<String> =
            accountsApi.getAccessToken(authorization = getClientDataHeader(clientId, clientSecret)).map(accessTokenMapper::mapFrom)

    override fun getCategories(accessToken: String): Observable<List<CategoryEntity>> =
            api.getCategories(authorization = getAccessTokenHeader(accessToken)).map { it.result.categories }.map { it.map(categoryMapper::mapFrom) }

    companion object {
        fun getAccessTokenHeader(accessToken: String): String {
            return "Bearer $accessToken"
        }

        fun getClientDataHeader(clientId: String, clientSecret: String): String {
            val encoded = Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)
            return "Basic $encoded"
        }
    }
}