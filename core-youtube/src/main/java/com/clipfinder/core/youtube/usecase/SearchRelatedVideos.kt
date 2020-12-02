package com.clipfinder.core.youtube.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.model.Resource
import com.clipfinder.core.usecase.SingleUseCaseWithArgs
import com.clipfinder.core.youtube.repo.IYoutubeRepo
import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Single

class SearchRelatedVideos(
    schedulers: RxSchedulers,
    private val repo: IYoutubeRepo
) : SingleUseCaseWithArgs<SearchRelatedVideos.Args, Resource<SearchListResponse>>(schedulers) {
    override fun run(args: Args): Single<Resource<SearchListResponse>> = repo
        .searchRelated(videoId = args.videoId, pageToken = args.pageToken)

    class Args(val videoId: String, val pageToken: String?)
}