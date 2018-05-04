package com.example.there.findclips.category.fragment

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.findclips.entities.Playlist

data class CategoryFragmentViewState(
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val playlists: ObservableArrayList<Playlist> = ObservableArrayList()
)