package com.example.spotifycategory

import android.databinding.ObservableField
import android.view.View
import com.example.coreandroid.model.spotify.Category

class CategoryView(
        val state: CategoryViewState,
        val category: Category,
        val onFavouriteBtnClickListener: View.OnClickListener
)

class CategoryViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val isSavedAsFavourite: ObservableField<Boolean> = ObservableField(false)
)