package com.example.coreandroid.view.binding

import android.databinding.BindingAdapter
import android.graphics.Color
import com.example.there.findclips.R
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
