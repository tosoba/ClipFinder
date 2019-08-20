package com.example.coreandroid.base.trackvideos

import com.example.coreandroid.base.model.BaseTrackUiModel
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.DataList
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.there.domain.usecase.base.DeleteTrackUseCase
import com.example.there.domain.usecase.base.InsertTrackUseCase
import com.example.there.domain.usecase.base.IsTrackSavedUseCase
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

abstract class BaseTrackVideosViewModel<Track : BaseTrackUiModel<TrackEntity>, TrackEntity>(
        initialState: TrackVideosViewState<Track>,
        private val insertTrack: InsertTrackUseCase<TrackEntity>,
        private val deleteTrack: DeleteTrackUseCase<TrackEntity>,
        private val isTrackSaved: IsTrackSavedUseCase<TrackEntity>
) : MvRxViewModel<TrackVideosViewState<Track>>(initialState) {

    init {
        loadTrackFavouriteState(initialState.tracks.value.last())
    }

    fun onBackPressed() = withState { state ->
        if (state.tracks.value.size < 2) {
            setState { copy(tracks = DataList(emptyList())) }
            return@withState
        }

        setState { copy(tracks = DataList(tracks.value.take(tracks.value.size - 1))) }
    }

    fun updateTrack(track: Track) {
        setState { copy(tracks = tracks.copyWithNewItems(track)) }
        loadTrackFavouriteState(track)
    }

    fun toggleTrackFavouriteState() = withState { state ->
        state.tracks.value.lastOrNull()?.let {
            if (state.isSavedAsFavourite.value) deleteFavouriteTrack(it)
            else addFavouriteTrack(it)
        }
    }

    private fun addFavouriteTrack(track: Track) = insertTrack(track.domainEntity, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({ setState { copy(isSavedAsFavourite = Data(true, LoadedSuccessfully)) } }, {
                setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
                Timber.e(it)
            })
            .disposeOnClear()

    private fun deleteFavouriteTrack(track: Track) = deleteTrack(track.domainEntity, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({ setState { copy(isSavedAsFavourite = Data(false, LoadedSuccessfully)) } }, {
                setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
                Timber.e(it)
            })
            .disposeOnClear()

    private fun loadTrackFavouriteState(track: Track) = withState { state ->
        if (state.isSavedAsFavourite.status is Loading) return@withState

        isTrackSaved(args = track.id, applySchedulers = false)
                .subscribeOn(Schedulers.io())
                .update(TrackVideosViewState<Track>::isSavedAsFavourite) { copy(isSavedAsFavourite = it) }
    }
}