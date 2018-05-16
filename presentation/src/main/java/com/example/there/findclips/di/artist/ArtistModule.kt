package com.example.there.findclips.di.artist

import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetAlbumsFromArtist
import com.example.there.domain.usecases.spotify.GetRelatedArtists
import com.example.there.domain.usecases.spotify.GetTopTracksFromArtist
import com.example.there.findclips.activities.artist.ArtistVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@ArtistScope
@Module
class ArtistModule {

    @Provides
    fun albumsFromArtistUseCase(repository: SpotifyRepository): GetAlbumsFromArtist = GetAlbumsFromArtist(AsyncTransformer(), repository)

    @Provides
    fun topTracksFromArtistUseCase(repository: SpotifyRepository): GetTopTracksFromArtist = GetTopTracksFromArtist(AsyncTransformer(), repository)

    @Provides
    fun relatedArtistsUseCase(repository: SpotifyRepository): GetRelatedArtists = GetRelatedArtists(AsyncTransformer(), repository)

    @Provides
    fun artistVMFactory(getAccessToken: GetAccessToken,
                        getAlbumsFromArtist: GetAlbumsFromArtist,
                        getTopTracksFromArtist: GetTopTracksFromArtist,
                        getRelatedArtists: GetRelatedArtists): ArtistVMFactory =
            ArtistVMFactory(getAccessToken, getAlbumsFromArtist, getTopTracksFromArtist, getRelatedArtists)
}