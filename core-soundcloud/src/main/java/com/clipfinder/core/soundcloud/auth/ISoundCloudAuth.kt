package com.clipfinder.core.soundcloud.auth

import io.reactivex.Single

interface ISoundCloudAuth {
    val clientId: Single<String>
}
