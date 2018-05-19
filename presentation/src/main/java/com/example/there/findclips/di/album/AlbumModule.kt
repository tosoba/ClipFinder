package com.example.there.findclips.di.album

import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetArtists
import com.example.there.domain.usecases.spotify.GetTracksFromAlbum
import com.example.there.domain.usecases.spotify.InsertAlbum
import com.example.there.findclips.activities.album.AlbumVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@AlbumScope
@Module
class AlbumModule {

    @Provides
    fun insertAlbumUseCase(repository: ISpotifyRepository): InsertAlbum = InsertAlbum(AsyncTransformer(), repository)

    @Provides
    fun tracksFromAlbumUseCase(repository: ISpotifyRepository): GetTracksFromAlbum = GetTracksFromAlbum(AsyncTransformer(), repository)

    @Provides
    fun albumVMFactory(getAccessToken: GetAccessToken,
                       getArtists: GetArtists,
                       getTracksFromAlbum: GetTracksFromAlbum): AlbumVMFactory = AlbumVMFactory(getAccessToken, getArtists, getTracksFromAlbum)
}