package com.example.soundclouddashboard.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.soundclouddashboard.domain.repo.ISoundCloudDashboardRepo
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetClientId(
    private val repo: ISoundCloudDashboardRepo,
    rxSchedulers: RxSchedulers
) : SingleUseCase<String>(rxSchedulers) {
    override val result: Single<String> get() = repo.clientId
}
