package com.example.spotifyrepo

import com.example.core.android.spotify.model.ext.single
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.SpotifyApi
import com.example.spotifyapi.model.AudioFeaturesApiModel
import com.example.spotifyapi.model.UserApiModel
import com.example.spotifyrepo.mapper.domain
import com.example.there.domain.entity.spotify.AudioFeaturesEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.entity.spotify.UserEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import io.reactivex.Single

class SpotifyRemoteRepo(
    preferences: SpotifyPreferences,
    accountsApi: SpotifyAccountsApi,
    private val api: SpotifyApi
) : BaseSpotifyRemoteRepo(accountsApi, preferences), ISpotifyRemoteDataStore {

    override val currentUser: Single<Resource<UserEntity>>
        get() = preferences.userPrivateAccessToken.single.flatMapValidElseThrow { token ->
            api.getCurrentUser(getAccessTokenHeader(token))
                .mapToResource(UserApiModel::domain)
        }

    override fun getAudioFeatures(
        trackEntity: TrackEntity
    ): Single<Resource<AudioFeaturesEntity>> = withTokenSingle { token ->
        api.getAudioFeatures(
            authorization = getAccessTokenHeader(token),
            trackId = trackEntity.id
        ).mapToResource(AudioFeaturesApiModel::domain)
    }
}
