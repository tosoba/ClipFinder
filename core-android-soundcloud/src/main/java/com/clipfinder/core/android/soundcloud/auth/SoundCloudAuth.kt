package com.clipfinder.core.android.soundcloud.auth

import android.annotation.SuppressLint
import android.util.Log
import com.clipfinder.core.soundcloud.auth.ISoundCloudAuth
import io.reactivex.Single
import org.jsoup.Jsoup
import java.lang.Exception
import java.net.URL

object SoundCloudAuth : ISoundCloudAuth {
    override val clientId: Single<String>
        @SuppressLint("SetJavaScriptEnabled")
        get() = Single.create { emitter ->
            try {
                Jsoup.connect("https://soundcloud.com")
                    .get()
                    .select("script")
                    .filter { scriptElement ->
                        scriptElement.hasAttr("src")
                            && scriptElement.attr("src").contains(".js")
                    }
                    .forEach { scriptElement ->
                        URL(scriptElement.attr("src"))
                            .openStream()
                            .reader()
                            .useLines { lines ->
                                lines.forEach forEachLine@{ line ->
                                    val clientIdIndex = line.indexOf("client_id=")
                                    if (clientIdIndex == -1) return@forEachLine
                                    emitter.onSuccess(
                                        line.substring(
                                            line.indexOf("=", clientIdIndex) + 1,
                                            line.indexOf("&", clientIdIndex)
                                        )
                                    )
                                }
                            }
                    }
                emitter.onError(ClientIdNotFoundException)
            } catch (ex: Exception) {
                emitter.onError(ex)
            }
        }

    object ClientIdNotFoundException : Throwable()
}
