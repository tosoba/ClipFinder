package com.example.there.domain.usecase.spotify

import com.example.there.domain.common.SymmetricFlowableTransformer
import com.example.there.domain.common.SymmetricSingleTransformer
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.UseCaseParams
import com.example.there.domain.usecase.base.CompletableUseCase
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Flowable
import io.reactivex.Single

class GetFavouriteAlbums(
        transformer: SymmetricFlowableTransformer<List<AlbumEntity>>,
        private val repository: ISpotifyRepository
) : FlowableUseCase<List<AlbumEntity>>(transformer) {

    override fun createFlowable(data: Map<String, Any?>?): Flowable<List<AlbumEntity>> = repository.favouriteAlbums
}

class InsertAlbum(
        transformer: CompletableTransformer,
        private val repository: ISpotifyRepository
) : CompletableUseCase(transformer) {

    override fun createCompletable(data: Map<String, Any?>?): Completable {
        val albumEntity = data?.get(UseCaseParams.PARAM_ALBUM) as? AlbumEntity
        return if (albumEntity != null) {
            repository.insertAlbum(albumEntity)
        } else {
            Completable.error { IllegalArgumentException("AlbumEntity must be provided.") }
        }
    }

    fun execute(albumEntity: AlbumEntity): Completable {
        val data = HashMap<String, AlbumEntity>().apply {
            put(UseCaseParams.PARAM_ALBUM, albumEntity)
        }
        return execute(withData = data)
    }
}

class IsAlbumSaved(
        transformer: SymmetricSingleTransformer<Boolean>,
        private val repository: ISpotifyRepository
) : SingleUseCase<Boolean>(transformer) {

    override fun createSingle(data: Map<String, Any?>?): Single<Boolean> {
        val albumEntity = data?.get(UseCaseParams.PARAM_ALBUM) as? AlbumEntity
        return if (albumEntity != null) {
            repository.isAlbumSaved(albumEntity)
        } else {
            Single.error { IllegalArgumentException("AlbumEntity must be provided.") }
        }
    }

    fun execute(albumEntity: AlbumEntity): Single<Boolean> {
        val data = HashMap<String, AlbumEntity>().apply {
            put(UseCaseParams.PARAM_ALBUM, albumEntity)
        }
        return execute(withData = data)
    }
}

class DeleteAlbum(
        transformer: CompletableTransformer,
        private val repository: ISpotifyRepository
) : CompletableUseCase(transformer) {

    override fun createCompletable(data: Map<String, Any?>?): Completable {
        val albumEntity = data?.get(UseCaseParams.PARAM_ALBUM) as? AlbumEntity
        return if (albumEntity != null) {
            repository.deleteAlbum(albumEntity)
        } else {
            Completable.error { IllegalArgumentException("AlbumEntity must be provided.") }
        }
    }

    fun execute(albumEntity: AlbumEntity): Completable {
        val data = HashMap<String, AlbumEntity>().apply {
            put(UseCaseParams.PARAM_ALBUM, albumEntity)
        }
        return execute(withData = data)
    }
}