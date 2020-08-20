package com.example.soundclouddashboard.domain.usecase

import com.clipfinder.core.android.soundcloud.preferences.SoundCloudPreferences
import com.example.soundclouddashboard.domain.repo.ISoundCloudDashboardRepo

class GetMixedSelections(
    private val getClientId: GetClientId,
    private val repo: ISoundCloudDashboardRepo,
    private val preferences: SoundCloudPreferences
) {

}
