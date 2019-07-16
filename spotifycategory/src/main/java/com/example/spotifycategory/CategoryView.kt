package com.example.spotifycategory

import android.view.View
import androidx.databinding.ObservableField
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