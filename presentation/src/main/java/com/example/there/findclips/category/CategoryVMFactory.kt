package com.example.there.findclips.category

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.AccessTokenUseCase
import com.example.there.domain.usecases.spotify.PlaylistsForCategoryUseCase

@Suppress("UNCHECKED_CAST")
class CategoryVMFactory(private val accessTokenUseCase: AccessTokenUseCase,
                        private val playlistsForCategoryUseCase: PlaylistsForCategoryUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = CategoryViewModel(accessTokenUseCase, playlistsForCategoryUseCase) as T
}