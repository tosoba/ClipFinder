package com.example.there.domain.repo.spotify

import com.example.core.model.Resource
import com.example.there.domain.entity.spotify.*
import io.reactivex.Single

interface ISpotifyRemoteDataStore {
    val currentUser: Single<Resource<UserEntity>>
}
