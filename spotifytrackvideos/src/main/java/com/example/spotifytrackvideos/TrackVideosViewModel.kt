package com.example.spotifytrackvideos

import com.example.coreandroid.base.trackvideos.BaseTrackVideosViewModel
import com.example.coreandroid.model.spotify.Track
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.DeleteTrack
import com.example.there.domain.usecase.spotify.InsertTrack
import com.example.there.domain.usecase.spotify.IsTrackSaved

class TrackVideosViewModel(
        insertTrack: InsertTrack,
        deleteTrack: DeleteTrack,
        isTrackSaved: IsTrackSaved
) : BaseTrackVideosViewModel<Track, TrackEntity>(insertTrack, deleteTrack, isTrackSaved)