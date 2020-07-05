package com.example.coreandroid.base.model

interface BaseTrackUiModel<TrackEntity> {
    val id: String
    val domainEntity: TrackEntity
}
