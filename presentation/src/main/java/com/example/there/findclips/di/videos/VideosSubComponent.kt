package com.example.there.findclips.di.videos

import com.example.there.findclips.videoslist.VideosListActivity
import dagger.Subcomponent

@VideosScope
@Subcomponent(modules = [VideosModule::class])
interface VideosSubComponent {
    fun inject(videosListActivity: VideosListActivity)
}