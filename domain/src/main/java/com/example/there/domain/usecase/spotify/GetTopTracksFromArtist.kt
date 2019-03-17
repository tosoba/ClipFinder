package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Single
import javax.inject.Inject

class GetTopTracksFromArtist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<String, List<TrackEntity>>(schedulersProvider) {

    override fun createSingle(input: String): Single<List<TrackEntity>> = repository.getTopTracksFromArtist(input)
}