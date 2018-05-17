package com.example.there.findclips.di.album

import com.example.there.findclips.activities.album.AlbumActivity
import dagger.Subcomponent

@AlbumScope
@Subcomponent(modules = [AlbumModule::class])
interface AlbumSubComponent {
    fun inject(albumActivity: AlbumActivity)
}