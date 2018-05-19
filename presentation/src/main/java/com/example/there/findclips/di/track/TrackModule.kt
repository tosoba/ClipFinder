package com.example.there.findclips.di.track

import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.spotify.*
import com.example.there.findclips.fragments.track.TrackVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@TrackScope
@Module
class TrackModule {

    @Provides
    fun insertTrackUseCase(repository: ISpotifyRepository): InsertTrack = InsertTrack(AsyncTransformer(), repository)

    @Provides
    fun albumUseCase(repository: ISpotifyRepository): GetAlbum = GetAlbum(AsyncTransformer(), repository)

    @Provides
    fun similarTracksUseCase(repository: ISpotifyRepository): GetSimilarTracks = GetSimilarTracks(AsyncTransformer(), repository)

    @Provides
    fun trackVMFactory(getAccessToken: GetAccessToken,
                       getAlbum: GetAlbum,
                       getArtists: GetArtists,
                       getSimilarTracks: GetSimilarTracks): TrackVMFactory =
            TrackVMFactory(getAccessToken, getAlbum, getArtists, getSimilarTracks)
}