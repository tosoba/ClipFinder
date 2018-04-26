package com.example.there.findclips.di.videos

import com.example.there.findclips.videos.VideosActivity
import dagger.Subcomponent

@VideosScope
@Subcomponent(modules = [VideosModule::class])
interface VideosSubComponent {
    fun inject(videosActivity: VideosActivity)
}