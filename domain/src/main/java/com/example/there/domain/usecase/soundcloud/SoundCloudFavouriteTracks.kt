package com.example.there.domain.usecase.soundcloud

import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudDbDataStore
import com.example.there.domain.usecase.base.DeleteTrackUseCase
import com.example.there.domain.usecase.base.GetFavouriteTracksUseCase
import com.example.there.domain.usecase.base.InsertTrackUseCase
import com.example.there.domain.usecase.base.IsTrackSavedUseCase

class GetFavouriteSoundCloudTracks(
    schedulers: RxSchedulers,
    repository: ISoundCloudDbDataStore
) : GetFavouriteTracksUseCase<SoundCloudTrackEntity>(schedulers, repository)

class InsertSoundCloudTrack(
    schedulers: RxSchedulers,
    repository: ISoundCloudDbDataStore
) : InsertTrackUseCase<SoundCloudTrackEntity>(schedulers, repository)

class IsSoundCloudTrackSaved(
    schedulers: RxSchedulers,
    repository: ISoundCloudDbDataStore
) : IsTrackSavedUseCase<SoundCloudTrackEntity>(schedulers, repository)

class DeleteSoundCloudTrack(
    schedulers: RxSchedulers,
    repository: ISoundCloudDbDataStore
) : DeleteTrackUseCase<SoundCloudTrackEntity>(schedulers, repository)
