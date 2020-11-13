package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.model.SpotifySearchResult
import com.clipfinder.core.spotify.model.SpotifySearchType
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class SearchSpotify(
    schedulers: RxSchedulers,
    private val auth: ISpotifyAuth,
    private val repo: ISpotifyRepo
) : SingleUseCaseWithArgs<SearchSpotify.Args, Resource<SpotifySearchResult>>(schedulers) {
    override fun run(args: Args): Single<Resource<SpotifySearchResult>> = auth
        .authorize()
        .andThen(when (args) {
            is Args.Initial -> repo.search(args.query, offset = 0, type = SpotifySearchType.ALL)
            is Args.More -> repo.search(args.query, offset = args.offset, type = args.type.value)
        })

    sealed class Args {
        abstract val query: String

        class Initial(override val query: String) : Args()
        class More(override val query: String, val offset: Int, val type: SpotifySearchType) : Args()
    }
}
