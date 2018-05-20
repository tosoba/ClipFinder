package com.example.there.findclips.di.playlist

import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetPlaylistTracks
import com.example.there.domain.usecases.spotify.InsertSpotifyPlaylist
import com.example.there.findclips.activities.playlist.PlaylistVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@PlaylistScope
@Module
class PlaylistModule {

    @Provides
    fun insertSpotifyPlaylistUseCase(repository: ISpotifyRepository): InsertSpotifyPlaylist = InsertSpotifyPlaylist(AsyncTransformer(), repository)

    @Provides
    fun playlistTracksUseCase(repository: ISpotifyRepository): GetPlaylistTracks = GetPlaylistTracks(AsyncTransformer(), repository)

    @Provides
    fun playlistVMFactory(getAccessToken: GetAccessToken, getPlaylistTracks: GetPlaylistTracks, insertSpotifyPlaylist: InsertSpotifyPlaylist) =
            PlaylistVMFactory(getAccessToken, getPlaylistTracks, insertSpotifyPlaylist)
}