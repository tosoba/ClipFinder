package com.example.there.findclips.di.trackvideos

import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.spotify.InsertTrack
import com.example.there.findclips.activities.trackvideos.TrackVideosVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@TrackVideosScope
@Module
class TrackVideosModule {

    @Provides
    fun insertTrackUseCase(repository: ISpotifyRepository): InsertTrack = InsertTrack(AsyncTransformer(), repository)

    @Provides
    fun trackVideosVMFactory(insertTrack: InsertTrack): TrackVideosVMFactory = TrackVideosVMFactory(insertTrack)
}