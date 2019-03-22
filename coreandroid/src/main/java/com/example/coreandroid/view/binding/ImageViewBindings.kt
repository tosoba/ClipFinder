package com.example.coreandroid.view.binding

import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.coreandroid.R
import com.example.coreandroid.view.imageview.ImageViewSrc

@BindingAdapter("imageUrl")
fun bindImageUrl(view: ImageView, url: String?) {
    if (url != null && !url.isEmpty()) {
        Glide.with(view.context)
                .load(url)
                .apply(RequestOptions()
                        .fitCenter()
                        .error(R.drawable.placeholder))
                .into(view)
    } else {
        view.setImageResource(R.drawable.placeholder)
    }
}

@BindingAdapter("imageSrc")
fun bindImageSrc(view: ImageView, src: ImageViewSrc?) = src?.let {
    if (it.iconUrl != null) {
        Glide.with(view.context)
                .load(it.iconUrl)
                .apply(RequestOptions()
                        .fitCenter()
                        .placeholder(it.loadingPlaceholder)
                        .error(it.errorPlaceholder))
                .into(view)
    } else {
        view.setImageResource(it.errorPlaceholder)
    }
}

@BindingAdapter("userThumbnailUrl")
fun bindUserImageUrl(view: ImageView, url: String?) = Glide.with(view.context)
        .load(url)
        .apply(RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder))
        .into(view)

@BindingAdapter("onClickIfNotNull")
fun bindOnClick(
        view: ImageView,
        onClickListener: View.OnClickListener?
) = onClickListener?.let { view.setOnClickListener(it) } ?: Unit
