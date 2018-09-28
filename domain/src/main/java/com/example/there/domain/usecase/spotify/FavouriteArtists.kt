package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricFlowableTransformer
import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.CompletableUseCase
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Flowable
import io.reactivex.Single

class GetFavouriteArtists(
        transformer: SymmetricFlowableTransformer<List<ArtistEntity>>,
        private val repository: ISpotifyRepository
) : FlowableUseCase<List<ArtistEntity>>(transformer) {

    override fun createFlowable(data: Map<String, Any?>?): Flowable<List<ArtistEntity>> = repository.getFavouriteArtists()
}

class InsertArtist(
        transformer: CompletableTransformer,
        private val repository: ISpotifyRepository
) : CompletableUseCase(transformer) {

    override fun createCompletable(data: Map<String, Any?>?): Completable {
        val artistEntity = data?.get(UseCaseParams.PARAM_ARTIST) as? ArtistEntity
        return if (artistEntity != null) {
            repository.insertArtist(artistEntity)
        } else {
            Completable.error { IllegalArgumentException("ArtistEntity must be provided.") }
        }
    }

    fun execute(artistEntity: ArtistEntity): Completable {
        val data = HashMap<String, ArtistEntity>().apply {
            put(UseCaseParams.PARAM_ARTIST, artistEntity)
        }
        return execute(withData = data)
    }
}

class IsArtistSaved(
        transformer: SymmetricSingleTransformer<Boolean>,
        private val repository: ISpotifyRepository
) : SingleUseCase<Boolean>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<Boolean> {
        val artistEntity = data?.get(UseCaseParams.PARAM_ARTIST) as? ArtistEntity
        return if (artistEntity != null) {
            repository.isArtistSaved(artistEntity)
        } else {
            Single.error { IllegalArgumentException("ArtistEntity must be provided.") }
        }
    }

    fun execute(artistEntity: ArtistEntity): Single<Boolean> {
        val data = HashMap<String, ArtistEntity>().apply {
            put(UseCaseParams.PARAM_ARTIST, artistEntity)
        }
        return execute(withData = data)
    }
}

class DeleteArtist(
        transformer: CompletableTransformer,
        private val repository: ISpotifyRepository
) : CompletableUseCase(transformer) {

    override fun createCompletable(data: Map<String, Any?>?): Completable {
        val artistEntity = data?.get(UseCaseParams.PARAM_ARTIST) as? ArtistEntity
        return if (artistEntity != null) {
            repository.deleteArtist(artistEntity)
        } else {
            Completable.error { IllegalArgumentException("ArtistEntity must be provided.") }
        }
    }

    fun execute(artistEntity: ArtistEntity): Completable {
        val data = HashMap<String, ArtistEntity>().apply {
            put(UseCaseParams.PARAM_ARTIST, artistEntity)
        }
        return execute(withData = data)
    }
}