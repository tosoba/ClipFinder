package com.example.there.findclips.di.relatedvideos

import com.example.there.findclips.fragments.relatedvideos.RelatedVideosFragment
import dagger.Subcomponent

@RelatedVideosScope
@Subcomponent(modules = [RelatedVideosModule::class])
interface RelatedVideosSubComponent {
    fun inject(fragment: RelatedVideosFragment)
}