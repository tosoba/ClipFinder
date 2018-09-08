package com.example.there.findclips.fragment.search

import android.content.SearchRecentSuggestionsProvider

class SearchSuggestionProvider: SearchRecentSuggestionsProvider() {

    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.example.there.findclips.fragments.search.SearchSuggestionProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }
}