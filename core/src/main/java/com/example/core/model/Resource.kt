package com.example.core.model

/**
 * A sealed class to represent UI states associated with a resource.
 */
sealed class Resource<out T> {
    /**
     * A data class to represent the scenario where the resource is available without any errors
     */
    data class Success<out T>(val data: T) : Resource<T>()

    /**
     * A data class to represent the scenario where a resource may or may not be available due to an error
     */
    data class Error<out T, out E>(val error: E, val data: T? = null) : Resource<T>()
}



