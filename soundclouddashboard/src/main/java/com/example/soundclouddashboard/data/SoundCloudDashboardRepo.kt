package com.example.soundclouddashboard.data

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylistSelection
import com.clipfinder.soundcloud.api.SoundCloudApiV2
import com.clipfinder.soundcloud.api.model.mixed.selections.SoundCloudMixedSelectionsResponse
import com.example.soundclouddashboard.domain.repo.ISoundCloudDashboardRepo
import io.reactivex.Single

class SoundCloudDashboardRepo(
    private val context: Context,
    private val apiV2: SoundCloudApiV2
) : ISoundCloudDashboardRepo {

    override val clientId: Single<String>
        @SuppressLint("SetJavaScriptEnabled")
        get() = Single.create<String> {
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onLoadResource(view: WebView?, url: String?) {
                        super.onLoadResource(view, url)
                        if (url == null || !url.contains("client_id")) return
                        val clientIdStartIndex = url.indexOf('=', url.indexOf("client_id")) + 1
                        val clientIdEndIndex = url.indexOf('&', clientIdStartIndex)
                        it.onSuccess(url.run {
                            if (clientIdEndIndex == -1) substring(clientIdStartIndex)
                            else url.substring(clientIdStartIndex, clientIdEndIndex)
                        })
                    }
                }
                loadUrl("https://soundcloud.com/")
            }
        }

    override fun mixedSelections(clientId: String): Single<List<ISoundCloudPlaylistSelection>> = apiV2
        .mixedSelections(clientId)
        .map(SoundCloudMixedSelectionsResponse::collection)
}
