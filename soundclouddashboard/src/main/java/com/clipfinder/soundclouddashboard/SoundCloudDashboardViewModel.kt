package com.clipfinder.soundclouddashboard

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.android.soundcloud.model.SoundCloudPlaylistSelection
import com.clipfinder.core.ext.Timeout
import com.clipfinder.core.soundcloud.usecase.GetMixedSelections
import com.clipfinder.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.model.LoadingInProgress
import com.clipfinder.core.android.util.ext.retryLoadCollectionOnConnected
import com.clipfinder.core.model.invoke
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.ext.android.get
import java.util.concurrent.TimeUnit

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
        if (currentSelections is LoadingInProgress) return@withState
        getMixedSelections(timeout = Timeout(25L, TimeUnit.SECONDS))
            .observeOn(AndroidSchedulers.mainThread())
            .map { resource -> resource.map { selections -> selections.map(::SoundCloudPlaylistSelection) } }
            .updateLoadableWithCollectionResource(SoundCloudDashboardState::selections) { copy(selections = it) }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (selections) ->
            if (selections.retryLoadCollectionOnConnected) loadSelections()
        }
    }

    companion object : MvRxViewModelFactory<SoundCloudDashboardViewModel, SoundCloudDashboardState> {
        override fun create(
            viewModelContext: ViewModelContext, state: SoundCloudDashboardState
        ): SoundCloudDashboardViewModel = SoundCloudDashboardViewModel(
            state, viewModelContext.activity.get(), viewModelContext.app()
        )
    }
}
