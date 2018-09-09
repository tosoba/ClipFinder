package com.example.there.findclips.view.list

import android.databinding.ObservableField

data class LoadingItemViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false)
)