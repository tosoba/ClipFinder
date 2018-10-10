package com.example.there.findclips.fragment.spotifyitem.category

import android.databinding.ObservableField
import android.view.View
import com.example.there.findclips.model.entity.Category

class CategoryView(
        val state: CategoryViewState,
        val category: Category,
        val onFavouriteBtnClickListener: View.OnClickListener
)

class CategoryViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val isSavedAsFavourite: ObservableField<Boolean> = ObservableField(false)
)