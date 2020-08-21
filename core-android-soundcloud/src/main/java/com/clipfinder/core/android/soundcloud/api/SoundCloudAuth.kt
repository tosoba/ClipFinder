package com.clipfinder.core.android.soundcloud.api

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import com.clipfinder.core.soundcloud.api.ISoundCloudAuth
import io.reactivex.Single

class SoundCloudAuth(private val context: Context) : ISoundCloudAuth {
    override val clientId: Single<String>
        @SuppressLint("SetJavaScriptEnabled")
        get() = Single.create {
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
                            else substring(clientIdStartIndex, clientIdEndIndex)
                        })
                    }
                }
                loadUrl("https://soundcloud.com/")
            }
        }
}
