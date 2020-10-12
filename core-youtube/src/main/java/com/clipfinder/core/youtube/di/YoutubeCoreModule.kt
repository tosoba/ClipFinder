package com.clipfinder.core.youtube.di

import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import org.koin.dsl.module

val youtubeCoreModule = module {
    single<YouTube> { YouTube.Builder(NetHttpTransport(), GsonFactory(), null).build() }
}
