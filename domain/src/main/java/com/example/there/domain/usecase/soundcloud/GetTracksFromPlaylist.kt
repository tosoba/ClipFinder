package com.example.there.domain.usecase.soundcloud

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.usecase.SingleUseCaseWithArgs
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudRemoteDataStore
import io.reactivex.Single

class GetTracksFromPlaylist(
    schedulers: RxSchedulers,
    private val remote: ISoundCloudRemoteDataStore
) : SingleUseCaseWithArgs<String, List<SoundCloudTrackEntity>>(schedulers) {
    override fun run(args: String): Single<List<SoundCloudTrackEntity>> = remote.getTracksFromPlaylist(args)
}
