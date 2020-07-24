package com.example.core.android.view.binding

import android.graphics.Color
import androidx.databinding.BindingAdapter
import com.example.coreandroid.R
import com.lopei.collageview.CollageView

class CollageViewState(
    val urls: Array<String>,
    val useFirstAsHeader: Boolean = true,
    val photoMargin: Int = 0,
    val photoPadding: Int = 0,
    val backgroundColor: Int = Color.TRANSPARENT,
    val photoFrameColor: Int = Color.TRANSPARENT,
    val placeholderResource: Int = R.drawable.video_playlist_thumbnail,
    val defaultPhotosForLine: Int = 2,
    val useCards: Boolean = false
)

@BindingAdapter("collageViewState")
fun bindCollageViewState(
    collageView: CollageView,
    viewState: CollageViewState
) = with(viewState) {
    collageView.photoMargin(photoMargin)
        .photoPadding(photoPadding)
        .backgroundColor(backgroundColor)
        .photoFrameColor(photoFrameColor)
        .useFirstAsHeader(useFirstAsHeader)
        .defaultPhotosForLine(defaultPhotosForLine)
        .placeHolder(placeholderResource)
        .useCards(useCards)
        .loadPhotos(urls)
}
