package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable

class GetFavouriteArtists(transformer: SymmetricObservableTransformer<List<ArtistEntity>>,
                          private val repository: ISpotifyRepository) : ObservableUseCase<List<ArtistEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<ArtistEntity>> = repository.getFavouriteArtists().toObservable()
}

class InsertArtist(transformer: SymmetricObservableTransformer<Unit>,
                   private val repository: ISpotifyRepository) : ObservableUseCase<Unit>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<Unit> {
        val artistEntity = data?.get(UseCaseParams.PARAM_ARTIST) as? ArtistEntity
        return if (artistEntity != null) {
            repository.insertArtist(artistEntity).toObservable()
        } else {
            Observable.error { IllegalArgumentException("ArtistEntity must be provided.") }
        }
    }

    fun execute(artistEntity: ArtistEntity): Observable<Unit> {
        val data = HashMap<String, ArtistEntity>().apply {
            put(UseCaseParams.PARAM_ARTIST, artistEntity)
        }
        return execute(withData = data)
    }
}