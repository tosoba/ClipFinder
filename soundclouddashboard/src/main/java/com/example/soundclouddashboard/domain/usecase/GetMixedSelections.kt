package com.example.soundclouddashboard.domain.usecase

import com.clipfinder.core.android.soundcloud.preferences.SoundCloudPreferences
import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylistSelection
import com.clipfinder.soundcloud.api.SoundCloudAuth
import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.soundclouddashboard.domain.repo.ISoundCloudDashboardRepo
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetMixedSelections(
    private val getClientId: GetClientId,
    private val repo: ISoundCloudDashboardRepo,
    private val preferences: SoundCloudPreferences,
    rxSchedulers: RxSchedulers
) : SingleUseCase<Resource<List<ISoundCloudPlaylistSelection>>>(rxSchedulers) {
    override val result: Single<Resource<List<ISoundCloudPlaylistSelection>>>
        get() = repo.mixedSelections(SoundCloudAuth.key).map { Resource.Success(it) }
}
