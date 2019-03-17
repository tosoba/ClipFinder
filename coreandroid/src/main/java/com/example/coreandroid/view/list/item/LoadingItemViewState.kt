package com.example.coreandroid.view.list.item

import android.databinding.ObservableField

class LoadingItemViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false)
)