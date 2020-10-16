package com.example.soundclouddashboard.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.android.soundcloud.model.SoundCloudPlaylistSelection
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.Loading
import com.example.core.android.model.retryLoadItemsOnNetworkAvailable
import com.example.core.android.util.ext.observeNetworkConnectivity
import com.example.soundclouddashboard.domain.usecase.GetMixedSelections
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.ext.android.inject

class SoundCloudDashboardViewModel(
    initialState: SoundCloudDashboardState,
    private val getMixedSelections: GetMixedSelections,
    context: Context
) : MvRxViewModel<SoundCloudDashboardState>(initialState) {

    init {
        loadSelections()
        handleConnectivityChanges(context)
    }

    fun loadSelections() = withState { (currentSelections) ->
        if (currentSelections.status is Loading) return@withState

        getMixedSelections(applySchedulers = false)
            .observeOn(AndroidSchedulers.mainThread())
            .map { resource ->
                resource.map { selections ->
                    selections.map { SoundCloudPlaylistSelection(it) }
                }
            }
            .updateWithResource(SoundCloudDashboardState::selections) { copy(selections = it) }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context
            .observeNetworkConnectivity {
                withState { (selections) ->
                    if (selections.retryLoadItemsOnNetworkAvailable) loadSelections()
                }
            }
            .disposeOnClear()
    }

    companion object : MvRxViewModelFactory<SoundCloudDashboardViewModel, SoundCloudDashboardState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SoundCloudDashboardState
        ): SoundCloudDashboardViewModel {
            val getMixedSelections: GetMixedSelections by viewModelContext.activity.inject()
            return SoundCloudDashboardViewModel(state, getMixedSelections, viewModelContext.app())
        }
    }
}
