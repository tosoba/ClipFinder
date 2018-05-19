package com.example.there.findclips.di.trackvideos

import com.example.there.findclips.activities.trackvideos.TrackVideosActivity
import dagger.Subcomponent

@TrackVideosScope
@Subcomponent(modules = [TrackVideosModule::class])
interface TrackVideosSubComponent {
    fun inject(trackVideosActivity: TrackVideosActivity)
}