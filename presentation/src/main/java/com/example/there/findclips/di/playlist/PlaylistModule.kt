package com.example.there.findclips.di.playlist

import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.usecases.spotify.AccessTokenUseCase
import com.example.there.domain.usecases.spotify.PlaylistTracksUseCase
import com.example.there.findclips.playlist.PlaylistVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@PlaylistScope
@Module
class PlaylistModule {

    @Provides
    fun playlistTracksUseCase(repository: SpotifyRepository): PlaylistTracksUseCase = PlaylistTracksUseCase(AsyncTransformer(), repository)

    @Provides
    fun playlistVMFactory(accessTokenUseCase: AccessTokenUseCase, playlistTracksUseCase: PlaylistTracksUseCase) =
            PlaylistVMFactory(accessTokenUseCase, playlistTracksUseCase)
}