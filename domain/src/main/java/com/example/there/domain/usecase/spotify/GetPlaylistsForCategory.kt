package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.EntityPage
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Single
import javax.inject.Inject

class GetPlaylistsForCategory @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<GetPlaylistsForCategory.Input, EntityPage<PlaylistEntity>>(schedulersProvider) {

    class Input(
            val categoryId: String,
            val offset: Int
    )

    override fun createSingle(input: Input): Single<EntityPage<PlaylistEntity>> = repository.getPlaylistsForCategory(input.categoryId, input.offset)
}