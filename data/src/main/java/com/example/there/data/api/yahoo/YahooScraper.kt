package com.example.there.data.api.yahoo

import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton
import org.jsoup.Jsoup


@Singleton
class YahooScraper @Inject constructor() {
    fun getVideoIds(query: String): Observable<List<String>> = Observable.fromCallable {
        val document = Jsoup.connect(getUrlForQuery(query)).get()
        val videoLinks = document.select("a[data-rurl]")
        videoLinks.map { getIdFromUrl(it.absUrl("data-rurl")) }
    }

    private fun getIdFromUrl(url: String): String = url.substring(url.lastIndexOf('=') + 1)

    private fun getUrlForQuery(query: String): String =
            "https://video.search.yahoo.com/search/video?fr=sfp&fr2=sb-top-video.search.yahoo.com&ei=UTF-8&p=$query&vsite=youtube"
}