package com.example.there.domain.repo.spotify

import com.example.core.model.Resource
import com.example.there.domain.entity.Page
import com.example.there.domain.entity.spotify.*
import io.reactivex.Observable
import io.reactivex.Single

interface ISpotifyRemoteDataStore {
    val currentUser: Single<Resource<UserEntity>>
    fun getAudioFeatures(trackEntity: TrackEntity): Single<Resource<AudioFeaturesEntity>>
}
