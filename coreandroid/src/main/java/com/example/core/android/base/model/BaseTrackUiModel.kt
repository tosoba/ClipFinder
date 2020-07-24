package com.example.core.android.base.model

interface BaseTrackUiModel<TrackEntity> {
    val id: String
    val domainEntity: TrackEntity
}
