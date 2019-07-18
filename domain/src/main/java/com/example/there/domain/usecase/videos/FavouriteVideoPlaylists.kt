package com.example.there.domain.usecase.videos

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.repo.videos.IVideosDbDataStore
import com.example.there.domain.usecase.base.CompletableUseCaseWithArgs
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class GetFavouriteVideoPlaylists(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IVideosDbDataStore
) : FlowableUseCase<List<VideoPlaylistEntity>>(schedulersProvider) {

    override val result: Flowable<List<VideoPlaylistEntity>>
        get() = repository.favouritePlaylists
}

class InsertVideoPlaylist(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IVideosDbDataStore
) : SingleUseCaseWithArgs<VideoPlaylistEntity, Long>(schedulersProvider) {

    override fun run(args: VideoPlaylistEntity): Single<Long> = repository.insertPlaylist(args)
}

class DeleteVideoPlaylist(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: IVideosDbDataStore
) : CompletableUseCaseWithArgs<VideoPlaylistEntity>(schedulersProvider) {

    override fun run(args: VideoPlaylistEntity): Completable = repository.deleteVideoPlaylist(args)
}