package com.example.there.domain.usecases.spotify

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.entities.spotify.AlbumEntity
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.usecases.UseCaseParams
import com.example.there.domain.usecases.base.ObservableUseCase
import io.reactivex.Observable

class GetFavouriteAlbums(transformer: SymmetricObservableTransformer<List<AlbumEntity>>,
                         private val repository: ISpotifyRepository) : ObservableUseCase<List<AlbumEntity>>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<List<AlbumEntity>> = repository.getFavouriteAlbums().toObservable()
}

class InsertAlbum(transformer: SymmetricObservableTransformer<Unit>,
                  private val repository: ISpotifyRepository): ObservableUseCase<Unit>(transformer) {

    override fun createObservable(data: Map<String, Any?>?): Observable<Unit> {
        val albumEntity = data?.get(UseCaseParams.PARAM_ALBUM) as? AlbumEntity
        return if (albumEntity != null) {
            repository.insertAlbum(albumEntity).toObservable()
        } else {
            Observable.error { IllegalArgumentException("AlbumEntity must be provided.") }
        }
    }

    fun execute(albumEntity: AlbumEntity): Observable<Unit> {
        val data = HashMap<String, AlbumEntity>().apply {
            put(UseCaseParams.PARAM_ALBUM, albumEntity)
        }
        return execute(withData = data)
    }
}