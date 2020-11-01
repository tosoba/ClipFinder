package com.example.spotifyrepo

import com.example.core.android.spotify.model.ext.single
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotifyapi.SpotifyApi
import com.example.spotifyapi.model.UserApiModel
import com.example.spotifyrepo.mapper.domain
import com.example.there.domain.entity.spotify.UserEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import io.reactivex.Single

class SpotifyRemoteRepo(
    preferences: SpotifyPreferences,
    private val api: SpotifyApi
) : BaseSpotifyRemoteRepo(preferences), ISpotifyRemoteDataStore {

    override val currentUser: Single<Resource<UserEntity>>
        get() = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
            api.getCurrentUser(getAccessTokenHeader(token))
                .mapToResource(UserApiModel::domain)
        }
}
