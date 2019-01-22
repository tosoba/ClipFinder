package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable

class GetFeaturedPlaylists(
        transformer: SymmetricObservableTransformer<List<PlaylistEntity>>,
        private val repository: ISpotifyRepository
) : ObservableUseCase<List<PlaylistEntity>>(transformer) {
    override fun createObservable(data: Map<String, Any?>?): Observable<List<PlaylistEntity>> = repository.featuredPlaylists
}