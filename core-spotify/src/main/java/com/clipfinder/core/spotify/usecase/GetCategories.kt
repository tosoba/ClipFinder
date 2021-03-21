package com.clipfinder.core.spotify.usecase

import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.model.ISpotifyCategory
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.model.UseCaseWithArgs
import io.reactivex.Single

class GetCategories(
    private val repo: ISpotifyRepo
) : UseCaseWithArgs<Int, Single<Resource<Paged<List<ISpotifyCategory>>>>> {
    override fun run(args: Int): Single<Resource<Paged<List<ISpotifyCategory>>>> = repo.getCategories(offset = args)
}
