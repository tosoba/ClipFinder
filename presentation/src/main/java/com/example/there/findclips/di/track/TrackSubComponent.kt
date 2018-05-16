package com.example.there.findclips.di.track

import com.example.there.findclips.fragments.track.TrackFragment
import dagger.Subcomponent

@Subcomponent(modules = [TrackModule::class])
interface TrackSubComponent {
    fun inject(fragment: TrackFragment)
}