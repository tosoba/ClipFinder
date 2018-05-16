package com.example.there.findclips.di.playlist

import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetPlaylistTracks
import com.example.there.findclips.activities.playlist.PlaylistVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@PlaylistScope
@Module
class PlaylistModule {

    @Provides
    fun playlistTracksUseCase(repository: SpotifyRepository): GetPlaylistTracks = GetPlaylistTracks(AsyncTransformer(), repository)

    @Provides
    fun playlistVMFactory(getAccessToken: GetAccessToken, getPlaylistTracks: GetPlaylistTracks) =
            PlaylistVMFactory(getAccessToken, getPlaylistTracks)
}