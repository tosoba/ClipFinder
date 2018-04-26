package com.example.there.findclips.search

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecase.spotify.AccessTokenUseCase

class SearchViewModelFactory(private val accessTokenUseCase: AccessTokenUseCase) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(accessTokenUseCase) as T
    }
}