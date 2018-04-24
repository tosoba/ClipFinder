package com.example.there.findclips.dashboard

import com.example.there.domain.entities.AccessTokenEntity
import com.example.there.domain.usecase.AccessTokenUseCase
import com.example.there.domain.usecase.CategoriesUseCase
import com.example.there.domain.usecase.FeaturedPlaylistsUseCase
import com.example.there.findclips.base.BaseViewModel

class DashboardViewModel(accessTokenUseCase: AccessTokenUseCase,
                         private val featuredPlaylistsUseCase: FeaturedPlaylistsUseCase,
                         private val categoriesUseCase: CategoriesUseCase) : BaseViewModel(accessTokenUseCase) {

    val viewState: DashboardViewState = DashboardViewState()

    fun loadDashboardData(accessToken: AccessTokenEntity?) {
        if (accessToken != null && accessToken.isValid) {
            accessTokenLiveData.value = accessToken
            loadData(accessTokenLiveData.value!!)
        } else {
            loadAccessToken { loadData(it) }
        }
    }

    private fun loadData(accessToken: AccessTokenEntity) {
        loadCategories(accessToken)
        loadFeaturedPlaylists(accessToken)
    }

    private fun loadCategories(accessToken: AccessTokenEntity) {
        viewState.categoriesLoadingInProgress.set(true)
        addDisposable(categoriesUseCase.getCategories(accessToken)
                .doFinally { viewState.categoriesLoadingInProgress.set(false) }
                .subscribe({
                    viewState.addCategoriesSorted(it)
                }, {
                    errorState.value = it
                    handleErrors(it, onErrorsResolved = this::loadDashboardData)
                }))
    }

    private fun loadFeaturedPlaylists(accessToken: AccessTokenEntity) {
        viewState.featuredPlaylistsLoadingInProgress.set(true)
        addDisposable(featuredPlaylistsUseCase.getFeaturedPlaylists(accessToken)
                .doFinally { viewState.featuredPlaylistsLoadingInProgress.set(false) }
                .subscribe({
                    viewState.addFeaturedPlaylistsSorted(it)
                }, {
                    errorState.value = it
                    handleErrors(it, onErrorsResolved = this::loadDashboardData)
                }))
    }
}