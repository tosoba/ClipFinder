package com.example.there.findclips.view.viewflipper

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.there.findclips.R
import com.example.there.findclips.databinding.ImageViewListItemBinding

class PlaylistThumbnailFlipperAdapter(val thumbnailUrls: List<String>) : BaseAdapter() {

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(parent?.context)
        val url = thumbnailUrls[position]
        val holder: ViewHolder = if (view == null) {
            val binding: ImageViewListItemBinding = DataBindingUtil.inflate(inflater, R.layout.image_view_list_item, parent, false)
            ViewHolder(binding.apply { this.url = url })
            return binding.root
        } else {
            view.tag as ViewHolder
        }

        holder.binding.url = url
        return view
    }

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = thumbnailUrls.size

    class ViewHolder(val binding: ImageViewListItemBinding)
}