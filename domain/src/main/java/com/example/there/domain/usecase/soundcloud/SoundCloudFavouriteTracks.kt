package com.example.there.domain.usecase.soundcloud

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.repo.soundcloud.ISoundCloudDbDataStore
import com.example.there.domain.usecase.base.DeleteTrackUseCase
import com.example.there.domain.usecase.base.GetFavouriteTracksUseCase
import com.example.there.domain.usecase.base.InsertTrackUseCase
import com.example.there.domain.usecase.base.IsTrackSavedUseCase

class GetFavouriteSoundCloudTracks(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISoundCloudDbDataStore
) : GetFavouriteTracksUseCase<SoundCloudTrackEntity>(schedulersProvider, repository)

class InsertSoundCloudTrack(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISoundCloudDbDataStore
) : InsertTrackUseCase<SoundCloudTrackEntity>(schedulersProvider, repository)

class IsSoundCloudTrackSaved(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISoundCloudDbDataStore
) : IsTrackSavedUseCase<SoundCloudTrackEntity>(schedulersProvider, repository)

class DeleteSoundCloudTrack(
        schedulersProvider: UseCaseSchedulersProvider,
        repository: ISoundCloudDbDataStore
) : DeleteTrackUseCase<SoundCloudTrackEntity>(schedulersProvider, repository)