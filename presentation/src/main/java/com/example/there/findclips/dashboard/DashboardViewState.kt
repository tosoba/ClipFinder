package com.example.there.findclips.dashboard

import android.databinding.ObservableArrayList
import com.example.there.domain.entities.CategoryEntity

data class DashboardViewState(
        val categories: ObservableArrayList<CategoryEntity> = ObservableArrayList()
)