package com.clipfinder.core.youtube.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.model.Resource
import com.clipfinder.core.usecase.SingleUseCaseWithArgs
import com.clipfinder.core.youtube.repo.IYoutubeRepo
import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Single

class SearchVideos(
    private val repo: IYoutubeRepo,
    schedulers: RxSchedulers
) : SingleUseCaseWithArgs<SearchVideos.Args, Resource<SearchListResponse>>(schedulers) {
    override fun run(args: Args): Single<Resource<SearchListResponse>> = repo
        .search(args.query, args.pageToken)

    class Args(val query: String, val pageToken: String? = null)
}
