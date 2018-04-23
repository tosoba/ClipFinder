package com.example.there.findclips.bindings

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.example.there.findclips.R
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun bindImageUrl(view: ImageView, url: String?) {
    if (url != null && !url.isEmpty()) {
        Picasso.with(view.context)
                .load(url)
                .error(R.drawable.placeholder)
                .into(view)
    } else {
        view.setImageResource(R.drawable.placeholder)
    }
}