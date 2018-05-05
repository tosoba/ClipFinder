package com.example.there.findclips.playlist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.AccessTokenUseCase
import com.example.there.domain.usecases.spotify.PlaylistTracksUseCase

@Suppress("UNCHECKED_CAST")
class PlaylistVMFactory(private val accessTokenUseCase: AccessTokenUseCase,
                        private val playlistTracksUseCase: PlaylistTracksUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            PlaylistViewModel(accessTokenUseCase, playlistTracksUseCase) as T
}