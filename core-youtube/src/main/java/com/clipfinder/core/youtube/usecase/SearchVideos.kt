package com.clipfinder.core.youtube.usecase

import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.UseCaseWithArgs
import com.clipfinder.core.youtube.repo.IYoutubeRepo
import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Single

class SearchVideos(private val repo: IYoutubeRepo) :
    UseCaseWithArgs<SearchVideos.Args, Single<Resource<SearchListResponse>>> {
    override fun run(args: Args): Single<Resource<SearchListResponse>> =
        repo.searchVideos(args.query, args.pageToken)

    class Args(val query: String, val pageToken: String? = null)
}
