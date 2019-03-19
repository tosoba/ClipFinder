package com.example.spotifysearch

import android.content.SearchRecentSuggestionsProvider

class SearchSuggestionProvider : SearchRecentSuggestionsProvider() {

    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.example.spotifysearch.SearchSuggestionProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }
}