package com.example.there.findclips.category

import android.databinding.ObservableField
import android.view.View
import com.example.there.findclips.entities.Category

data class CategoryView(
        val state: CategoryViewState,
        val category: Category,
        val onFavouriteBtnClickListener: View.OnClickListener
)

data class CategoryViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false)
)