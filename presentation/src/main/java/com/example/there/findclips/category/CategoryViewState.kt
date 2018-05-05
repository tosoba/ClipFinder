package com.example.there.findclips.category

import android.databinding.ObservableField

data class CategoryViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false)
)