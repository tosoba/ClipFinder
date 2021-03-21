package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.UseCaseWithArgs
import com.clipfinder.core.spotify.model.SpotifySearchResult
import com.clipfinder.core.spotify.model.SpotifySearchType
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import io.reactivex.Single

class SearchSpotify(
    private val repo: ISpotifyRepo
) : UseCaseWithArgs<SearchSpotify.Args, Single<Resource<SpotifySearchResult>>> {
    override fun run(args: Args): Single<Resource<SpotifySearchResult>> = when (args) {
        is Args.Initial -> repo.search(args.query, offset = 0, type = SpotifySearchType.ALL)
        is Args.More -> repo.search(args.query, offset = args.offset, type = args.type.value)
    }

    sealed class Args {
        abstract val query: String

        class Initial(override val query: String) : Args()
        class More(override val query: String, val offset: Int, val type: SpotifySearchType) : Args()
    }
}
