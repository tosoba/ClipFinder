package com.example.there.findclips.dashboard

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecase.AccessTokenUseCase
import com.example.there.domain.usecase.CategoriesUseCase

class DashboardViewModelFactory(private val accessTokenUseCase: AccessTokenUseCase,
                                private val categoriesUseCase: CategoriesUseCase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardViewModel(accessTokenUseCase, categoriesUseCase) as T
    }
}