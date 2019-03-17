package com.example.there.domain.usecase.videos

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.base.CompletableUseCaseWithInput
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class GetFavouriteVideoPlaylists @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IVideosRepository
) : FlowableUseCase<List<VideoPlaylistEntity>>(schedulersProvider) {

    override val flowable: Flowable<List<VideoPlaylistEntity>>
        get() = repository.favouritePlaylists
}

class InsertVideoPlaylist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IVideosRepository
) : SingleUseCaseWithInput<VideoPlaylistEntity, Long>(schedulersProvider) {

    override fun createSingle(input: VideoPlaylistEntity): Single<Long> = repository.insertPlaylist(input)
}

class DeleteVideoPlaylist @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IVideosRepository
) : CompletableUseCaseWithInput<VideoPlaylistEntity>(schedulersProvider) {

    override fun createCompletable(input: VideoPlaylistEntity): Completable = repository.deleteVideoPlaylist(input)
}