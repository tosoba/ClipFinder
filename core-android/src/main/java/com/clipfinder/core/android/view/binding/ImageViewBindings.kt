package com.clipfinder.core.android.view.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.clipfinder.core.android.R
import com.clipfinder.core.android.view.imageview.ImageViewSrc

@BindingAdapter("imageUrl")
fun bindImageUrl(view: ImageView, url: String?) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(view.context)
            .load(url)
            .apply(RequestOptions().fitCenter().error(R.drawable.placeholder))
            .into(view)
    } else {
        view.setImageResource(R.drawable.placeholder)
    }
}

@BindingAdapter("imageSrc")
fun bindImageSrc(view: ImageView, src: ImageViewSrc?) =
    src?.let {
        if (it.iconUrl != null) {
            Glide.with(view.context)
                .load(it.iconUrl)
                .apply(
                    RequestOptions()
                        .fitCenter()
                        .placeholder(it.loadingPlaceholder)
                        .error(it.errorPlaceholder)
                )
                .into(view)
        } else {
            view.setImageResource(it.errorPlaceholder)
        }
    }

@BindingAdapter("userThumbnailUrl")
fun bindUserImageUrl(view: ImageView, url: String?) =
    Glide.with(view.context)
        .load(url)
        .apply(
            RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder)
        )
        .into(view)

@BindingAdapter(
    "glideImage",
    "glideImageError",
    "glideImageFallback",
    "glideImageLoading",
    "glideCircleCrop",
    requireAll = false
)
fun loadImage(
    view: ImageView,
    glideImage: String?,
    glideImageError: Int?,
    glideImageFallback: Int?,
    glideImageLoading: Int?,
    glideCircleCrop: Boolean?
) {
    Glide.with(view)
        .load(glideImage)
        .transition(withCrossFade())
        .run { glideImageError?.let { error(it) } ?: this }
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .run { glideImageFallback?.let { fallback(it) } ?: this }
        .apply(
            RequestOptions().fitCenter().run { glideImageLoading?.let { placeholder(it) } ?: this }
        )
        .apply {
            if (glideCircleCrop == true) circleCrop()
            into(view)
        }
}
