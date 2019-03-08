package com.example.there.findclips.spotify.spotifyitem.category

import android.databinding.ObservableField
import android.view.View
import com.example.there.findclips.model.entity.spotify.Category

class CategoryView(
        val state: CategoryViewState,
        val category: Category,
        val onFavouriteBtnClickListener: View.OnClickListener
)

class CategoryViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val isSavedAsFavourite: ObservableField<Boolean> = ObservableField(false)
)