package com.example.there.findclips.di.playlist

import com.example.there.findclips.playlist.PlaylistActivity
import dagger.Subcomponent

@PlaylistScope
@Subcomponent(modules = [PlaylistModule::class])
interface PlaylistSubComponent {
    fun inject(playlistActivity: PlaylistActivity)
}