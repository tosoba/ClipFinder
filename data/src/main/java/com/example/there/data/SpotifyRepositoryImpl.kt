package com.example.there.data

import com.example.there.data.api.SpotifyAccountsApi
import com.example.there.data.api.SpotifyApi
import com.example.there.data.entities.AccessTokenData
import com.example.there.data.entities.CategoryData
import com.example.there.domain.common.Mapper
import com.example.there.domain.SpotifyDataStore
import com.example.there.domain.SpotifyRepository
import com.example.there.domain.entities.AccessTokenEntity
import com.example.there.domain.entities.CategoryEntity
import io.reactivex.Observable

class SpotifyRepositoryImpl(api: SpotifyApi,
                            accountsApi: SpotifyAccountsApi,
                            categoryMapper: Mapper<CategoryData, CategoryEntity>,
                            accessTokenMapper: Mapper<AccessTokenData, AccessTokenEntity>) : SpotifyRepository {

    private val remoteSpotifyDataStore: SpotifyDataStore = RemoteSpotifyDataStore(api, accountsApi, categoryMapper, accessTokenMapper)

    override fun getAccessToken(clientId: String, clientSecret: String): Observable<AccessTokenEntity> =
            remoteSpotifyDataStore.getAccessToken(clientId, clientSecret)

    override fun getCategories(accessToken: String): Observable<List<CategoryEntity>> = remoteSpotifyDataStore.getCategories(accessToken)
}