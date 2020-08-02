package com.example.spotify.search.domain.repo

import com.example.core.model.Resource
import com.example.spotify.search.domain.model.SpotifySearchResult
import io.reactivex.Single

interface ISpotifySearchRepo {
    fun search(query: String, offset: Int, type: String): Single<Resource<SpotifySearchResult>>
}
