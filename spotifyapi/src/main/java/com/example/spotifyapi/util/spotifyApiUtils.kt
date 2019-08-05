package com.example.spotifyapi.util

import com.example.spotifyapi.models.ResultEnum

internal fun <T : ResultEnum> Array<T>.match(identifier: String) =
        firstOrNull { it.retrieveIdentifier().toString().equals(identifier, true) }