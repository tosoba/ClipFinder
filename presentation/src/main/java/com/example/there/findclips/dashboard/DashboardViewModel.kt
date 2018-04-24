package com.example.there.findclips.dashboard

import com.example.there.domain.entities.AccessTokenEntity
import com.example.there.domain.usecase.AccessTokenUseCase
import com.example.there.domain.usecase.CategoriesUseCase
import com.example.there.findclips.base.BaseViewModel
import com.example.there.findclips.util.SingleLiveEvent

class DashboardViewModel(accessTokenUseCase: AccessTokenUseCase,
                         private val categoriesUseCase: CategoriesUseCase) : BaseViewModel(accessTokenUseCase) {

    val viewState: DashboardViewState = DashboardViewState()
    val errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    fun loadDashboardData(accessToken: AccessTokenEntity?) {
        if (accessToken != null && accessToken.isValid) {
            accessTokenLiveData.value = accessToken
            loadData(accessTokenLiveData.value!!)
        } else {
            loadAccessToken { loadData(it) }
        }
    }

    private fun loadData(accessToken: AccessTokenEntity) = with(accessToken) {
        loadCategories(this)
    }

    private fun loadCategories(accessToken: AccessTokenEntity) {
        addDisposable(categoriesUseCase.getCategories(accessToken)
                .subscribe({
                    viewState.categories.addAll(it)
                }, {
                    errorState.value = it
                    handleErrors(it, onErrorsResolved = this::loadDashboardData)
                }))
    }
}