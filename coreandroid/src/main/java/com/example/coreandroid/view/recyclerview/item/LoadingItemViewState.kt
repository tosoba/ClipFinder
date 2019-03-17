package com.example.coreandroid.view.recyclerview.item

import android.databinding.ObservableField

class LoadingItemViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false)
)