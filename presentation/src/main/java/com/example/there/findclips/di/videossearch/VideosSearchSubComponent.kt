package com.example.there.findclips.di.videossearch

import com.example.there.findclips.search.videos.VideosSearchFragment
import dagger.Subcomponent

@VideosSearchScope
@Subcomponent(modules = [
    VideosSearchModule::class
])
interface VideosSearchSubComponent {
    fun inject(fragment: VideosSearchFragment)
}