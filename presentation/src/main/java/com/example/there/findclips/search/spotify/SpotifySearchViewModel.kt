package com.example.there.findclips.search.spotify

import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.findclips.base.BaseSpotifyViewModel

class SpotifySearchViewModel(accessTokenUseCase: AccessTokenUseCase) : BaseSpotifyViewModel(accessTokenUseCase) {

    val viewState: SpotifySearchViewState = SpotifySearchViewState()
}