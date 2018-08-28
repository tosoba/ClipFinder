package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.Transformer
import com.example.there.domain.entities.spotify.ArtistEntity
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.UseCase
import com.example.there.domain.usecases.UseCaseParams
import io.reactivex.Observable

class GetFavouriteArtists(transformer: Transformer<List<ArtistEntity>>,
                          private val repository: ISpotifyRepository) : UseCase<List<ArtistEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<ArtistEntity>> = repository.getFavouriteArtists().toObservable()
}

class InsertArtist(transformer: Transformer<Unit>,
                   private val repository: ISpotifyRepository) : UseCase<Unit>(transformer) {

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