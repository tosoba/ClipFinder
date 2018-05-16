package com.example.there.findclips.di.artist

import com.example.there.findclips.activities.artist.ArtistActivity
import dagger.Subcomponent

@ArtistScope
@Subcomponent(modules = [ArtistModule::class])
interface ArtistSubComponent {
    fun inject(artistActivity: ArtistActivity)
}