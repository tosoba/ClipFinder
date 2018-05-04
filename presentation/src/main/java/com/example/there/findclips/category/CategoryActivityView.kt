package com.example.there.findclips.category

import android.view.View
import com.example.there.findclips.entities.Category

data class CategoryActivityView(
        val category: Category,
        val onFavouriteBtnClickListener: View.OnClickListener
)