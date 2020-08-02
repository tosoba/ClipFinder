package com.example.spotifysearch.domain.repo

import com.example.core.model.Resource
import com.example.spotifysearch.domain.model.SpotifySearchResult
import io.reactivex.Single

interface ISpotifySearchRepo {
    fun search(query: String, offset: Int, type: String): Single<Resource<SpotifySearchResult>>
}
