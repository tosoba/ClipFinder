package com.example.there.findclips.view.binding

import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.there.findclips.R

@BindingAdapter("imageUrl")
fun bindImageUrl(view: ImageView, url: String?) {
    if (url != null && !url.isEmpty()) {
        Glide.with(view.context)
                .load(url)
                .apply(RequestOptions()
                        .fitCenter()
                        .error(R.drawable.placeholder))
                .into(view)
    }
}

@BindingAdapter("onClickIfNotNull")
fun bindOnClick(view: ImageView, onClickListener: View.OnClickListener?) {
    onClickListener?.let { view.setOnClickListener(it) }
}