package com.example.there.findclips.module

import com.example.main.MainViewModel
import com.example.spotifyplayer.SpotifyPlayerViewModel
import com.example.youtubeplayer.YoutubePlayerViewModel
import com.example.youtuberelatedvideos.RelatedVideosViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { SpotifyPlayerViewModel() }
    viewModel { RelatedVideosViewModel(get(), get()) }
    viewModel { YoutubePlayerViewModel() }
}
