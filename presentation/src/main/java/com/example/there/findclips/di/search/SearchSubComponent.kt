package com.example.there.findclips.di.search

import com.example.there.findclips.di.videos.VideosModule
import com.example.there.findclips.search.SearchFragment
import dagger.Subcomponent

@SearchScope
@Subcomponent(modules = [
    SearchModule::class,
    VideosModule::class
])
interface SearchSubComponent {
    fun inject(fragment: SearchFragment)
}