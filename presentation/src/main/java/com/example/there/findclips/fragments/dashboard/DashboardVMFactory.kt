package com.example.there.findclips.fragments.dashboard

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetCategories
import com.example.there.domain.usecases.spotify.GetDailyViralTracks
import com.example.there.domain.usecases.spotify.GetFeaturedPlaylists

class DashboardVMFactory(private val getAccessToken: GetAccessToken,
                         private val getFeaturedPlaylists: GetFeaturedPlaylists,
                         private val getCategories: GetCategories,
                         private val getDailyViralTracks: GetDailyViralTracks) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardViewModel(getAccessToken, getFeaturedPlaylists, getCategories, getDailyViralTracks) as T
    }
}