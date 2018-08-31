package com.example.there.findclips.di.modules.ui

import android.arch.lifecycle.ViewModelProvider
import com.example.there.findclips.activities.album.AlbumActivity
import com.example.there.findclips.activities.artist.ArtistActivity
import com.example.there.findclips.activities.category.CategoryActivity
import com.example.there.findclips.activities.main.MainActivity
import com.example.there.findclips.activities.player.PlayerActivity
import com.example.there.findclips.activities.playlist.PlaylistActivity
import com.example.there.findclips.activities.trackvideos.TrackVideosActivity
import com.example.there.findclips.activities.videoplaylist.VideoPlaylistActivity
import com.example.there.findclips.di.vm.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {
    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun albumActivity(): AlbumActivity

    @ContributesAndroidInjector
    abstract fun artistActivity(): ArtistActivity

    @ContributesAndroidInjector
    abstract fun categoryActivity(): CategoryActivity

    @ContributesAndroidInjector
    abstract fun playerActivity(): PlayerActivity

    @ContributesAndroidInjector
    abstract fun playlistActivity(): PlaylistActivity

    @ContributesAndroidInjector
    abstract fun trackVideosActivity(): TrackVideosActivity

    @ContributesAndroidInjector
    abstract fun videoPlaylistActivity(): VideoPlaylistActivity
}