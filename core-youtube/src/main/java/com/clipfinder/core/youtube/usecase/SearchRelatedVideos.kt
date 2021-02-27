package com.clipfinder.core.youtube.usecase

import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.UseCaseWithArgs
import com.clipfinder.core.youtube.repo.IYoutubeRepo
import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Single

class SearchRelatedVideos(
    private val repo: IYoutubeRepo
) : UseCaseWithArgs<SearchRelatedVideos.Args, Single<Resource<SearchListResponse>>> {
    override fun run(args: Args): Single<Resource<SearchListResponse>> = repo
        .searchRelated(videoId = args.videoId, pageToken = args.pageToken)

    class Args(val videoId: String, val pageToken: String?)
}
