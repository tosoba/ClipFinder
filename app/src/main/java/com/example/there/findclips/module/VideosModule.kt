package com.example.there.findclips.module

import com.example.there.domain.usecase.videos.*
import org.koin.dsl.module

val videosModule = module {
    factory { GetChannelsThumbnailUrls(get(), get()) }
    factory { SearchRelatedVideos(get(), get()) }
    factory { SearchVideos(get(), get(), get()) }
}