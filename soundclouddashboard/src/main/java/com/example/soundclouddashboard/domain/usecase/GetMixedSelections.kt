package com.example.soundclouddashboard.domain.usecase

import com.clipfinder.core.android.soundcloud.preferences.SoundCloudPreferences
import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylistSelection
import com.clipfinder.soundcloud.api.SoundCloudAuth
import com.example.core.ext.RxSchedulers
import com.example.soundclouddashboard.domain.repo.ISoundCloudDashboardRepo
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetMixedSelections(
    private val getClientId: GetClientId,
    private val repo: ISoundCloudDashboardRepo,
    private val preferences: SoundCloudPreferences,
    rxSchedulers: RxSchedulers
) : SingleUseCase<List<ISoundCloudPlaylistSelection>>(rxSchedulers) {
    override val result: Single<List<ISoundCloudPlaylistSelection>>
        get() = repo.mixedSelections(SoundCloudAuth.key)
}
