package com.example.there.findclips.dashboard

import com.example.there.domain.usecase.AccessTokenUseCase
import com.example.there.domain.usecase.CategoriesUseCase
import com.example.there.findclips.base.BaseViewModel
import com.example.there.findclips.util.SingleLiveEvent

class DashboardViewModel(accessTokenUseCase: AccessTokenUseCase,
                         private val categoriesUseCase: CategoriesUseCase) : BaseViewModel(accessTokenUseCase) {

    val viewState: DashboardViewState = DashboardViewState()
    val errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    fun loadDashboardData(accessToken: String?) {
        if (accessToken != null) {
            //TODO: what if accessToken is expired -> handle errors
            loadCategories(accessToken)
        } else {
            loadAccessToken {
                loadCategories(it)
            }
        }
    }

    private fun loadCategories(accessToken: String) {
        addDisposable(categoriesUseCase.getCategories(accessToken)
                .subscribe({
                    viewState.categories.addAll(it)
                }, {
                    errorState.value = it
                }))
    }

    private fun onAccessTokenExpired() {

    }
}