package com.example.coreandroid.view.viewflipper

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.coreandroid.R
import com.example.coreandroid.view.imageview.ImageViewSrc

class PlaylistThumbnailFlipperAdapter(val thumbnailUrls: List<String>) : BaseAdapter() {

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(parent?.context)
        val src = ImageViewSrc.with(thumbnailUrls[position], R.drawable.playlist_placeholder, R.drawable.error_placeholder)
        val holder: ViewHolder = if (view == null) {
            val binding: com.example.coreandroid.databinding.ImageViewListItemBinding = DataBindingUtil.inflate(inflater, R.layout.image_view_list_item, parent, false)
            ViewHolder(binding.apply { this.src = src })
            return binding.root
        } else {
            view.tag as ViewHolder
        }

        holder.binding.src = src
        return view
    }

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = thumbnailUrls.size

    class ViewHolder(val binding: ImageViewListItemBinding)
}