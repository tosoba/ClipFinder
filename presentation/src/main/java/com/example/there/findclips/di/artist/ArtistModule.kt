package com.example.there.findclips.di.artist

import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.spotify.*
import com.example.there.findclips.activities.artist.ArtistVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@ArtistScope
@Module
class ArtistModule {

    @Provides
    fun insertArtistUseCase(repository: ISpotifyRepository): InsertArtist = InsertArtist(AsyncTransformer(), repository)

    @Provides
    fun albumsFromArtistUseCase(repository: ISpotifyRepository): GetAlbumsFromArtist = GetAlbumsFromArtist(AsyncTransformer(), repository)

    @Provides
    fun topTracksFromArtistUseCase(repository: ISpotifyRepository): GetTopTracksFromArtist = GetTopTracksFromArtist(AsyncTransformer(), repository)

    @Provides
    fun relatedArtistsUseCase(repository: ISpotifyRepository): GetRelatedArtists = GetRelatedArtists(AsyncTransformer(), repository)

    @Provides
    fun artistVMFactory(getAccessToken: GetAccessToken,
                        getAlbumsFromArtist: GetAlbumsFromArtist,
                        getTopTracksFromArtist: GetTopTracksFromArtist,
                        getRelatedArtists: GetRelatedArtists): ArtistVMFactory =
            ArtistVMFactory(getAccessToken, getAlbumsFromArtist, getTopTracksFromArtist, getRelatedArtists)
}