package com.example.soundcloudrepo

import com.example.there.domain.entity.soundcloud.SoundCloudPlaylistEntity
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudDbDataStore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class SoundCloudDbDataStore @Inject constructor() : ISoundCloudDbDataStore {
    override val favouritePlaylists: Flowable<List<SoundCloudPlaylistEntity>>
        get() = Flowable.just(emptyList())

    override fun isPlaylistSaved(playlist: SoundCloudPlaylistEntity): Single<Boolean> = Single.just(false)
    override fun insertPlaylist(playlist: SoundCloudPlaylistEntity): Completable = Completable.complete()
    override fun deletePlaylist(playlist: SoundCloudPlaylistEntity): Completable = Completable.complete()

    override val favouriteTracks: Flowable<List<SoundCloudTrackEntity>>
        get() = Flowable.just(emptyList())
    override fun insertTrack(track: SoundCloudTrackEntity): Completable = Completable.complete()
    override fun isTrackSaved(track: SoundCloudTrackEntity): Single<Boolean> = Single.just(false)
    override fun deleteTrack(track: SoundCloudTrackEntity): Completable = Completable.complete()
}