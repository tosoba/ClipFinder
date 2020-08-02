package com.example.spotify.search.domain.usecase

import com.example.core.android.spotify.model.ext.SpotifySearchType
import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.spotify.search.domain.model.SpotifySearchResult
import com.example.spotify.search.domain.repo.ISpotifySearchRepo
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class SearchSpotify(
    schedulers: RxSchedulers,
    private val repo: ISpotifySearchRepo
) : SingleUseCaseWithArgs<SearchSpotify.Args, Resource<SpotifySearchResult>>(schedulers) {

    sealed class Args {
        abstract val query: String

        class Initial(override val query: String) : Args()
        class More(override val query: String, val offset: Int, val type: SpotifySearchType) : Args()
    }

    override fun run(args: Args): Single<Resource<SpotifySearchResult>> = when (args) {
        is Args.Initial -> repo.search(args.query, offset = 0, type = SpotifySearchType.ALL)
        is Args.More -> repo.search(args.query, args.offset, args.type.value)
    }
}
