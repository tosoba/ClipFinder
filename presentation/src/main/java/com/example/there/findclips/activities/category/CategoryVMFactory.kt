package com.example.there.findclips.activities.category

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetPlaylistsForCategory

@Suppress("UNCHECKED_CAST")
class CategoryVMFactory(private val getAccessToken: GetAccessToken,
                        private val getPlaylistsForCategory: GetPlaylistsForCategory) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = CategoryViewModel(getAccessToken, getPlaylistsForCategory) as T
}