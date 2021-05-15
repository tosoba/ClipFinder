package com.clipfinder.core.ext

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named

val <T : Enum<T>> Enum<T>.qualifier: Qualifier
    get() = named(name)
