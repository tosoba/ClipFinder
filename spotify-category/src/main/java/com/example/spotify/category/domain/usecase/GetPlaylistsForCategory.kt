package com.example.spotify.category.domain.usecase

import com.example.core.ext.RxSchedulers
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.spotify.category.domain.repo.ISpotifyCategoryRepo
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Single

class GetPlaylistsForCategory(
    schedulers: RxSchedulers,
    private val repo: ISpotifyCategoryRepo
) : SingleUseCaseWithArgs<GetPlaylistsForCategory.Args, Resource<Paged<List<PlaylistEntity>>>>(schedulers) {

    class Args(val categoryId: String, val offset: Int)

    override fun run(
        args: Args
    ): Single<Resource<Paged<List<PlaylistEntity>>>> = repo.getPlaylistsForCategory(
        args.categoryId,
        args.offset
    )
}
