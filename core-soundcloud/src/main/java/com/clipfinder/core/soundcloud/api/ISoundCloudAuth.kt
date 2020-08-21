package com.clipfinder.core.soundcloud.api

import io.reactivex.Single

interface ISoundCloudAuth {
    val clientId: Single<String>
}
