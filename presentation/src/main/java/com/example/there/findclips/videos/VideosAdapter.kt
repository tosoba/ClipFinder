package com.example.there.findclips.videos

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.findclips.R
import com.example.there.findclips.databinding.VideoItemBinding

class VideosAdapter(private val videos: List<VideoEntity>) : RecyclerView.Adapter<VideosAdapter.VideosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: VideoItemBinding = DataBindingUtil.inflate(inflater, R.layout.video_item, parent, false)
        return VideosViewHolder(binding)
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        holder.binding.video = videos[position]
    }

    class VideosViewHolder(val binding: VideoItemBinding) : RecyclerView.ViewHolder(binding.root)
}