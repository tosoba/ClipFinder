package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Single
import javax.inject.Inject

class GetCurrentUsersPlaylists @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<Int, EntityPage<PlaylistEntity>>(schedulersProvider) {

    override fun createSingle(input: Int): Single<EntityPage<PlaylistEntity>> = repository.getCurrentUsersPlaylists(offset = input)
}