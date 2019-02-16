package com.example.there.findclips.fragment.player.spotify

import android.graphics.Bitmap
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.util.ext.getBitmapSingle
import com.squareup.picasso.Picasso
import javax.inject.Inject

class SpotifyPlayerViewModel @Inject constructor() : BaseViewModel() {

    val viewState = SpotifyPlayerViewState()

    fun getBitmapSingle(
            picasso: Picasso,
            url: String,
            onSuccess: (Bitmap) -> Unit,
            onError: () -> Unit
    ) = addDisposable(picasso.getBitmapSingle(url, onSuccess, onError))

}