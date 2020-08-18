package com.example.soundclouddashboard.domain.repo

import io.reactivex.Single

interface ISoundCloudDashboardRepo {
    val clientId: Single<String>
}
