package com.example.there.findclips.dashboard.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.domain.entities.TopTrackEntity
import com.example.there.findclips.R
import com.example.there.findclips.databinding.TopTrackItemBinding

class TopTracksListAdapter(private val tracks: List<TopTrackEntity>) : RecyclerView.Adapter<TopTracksListAdapter.TopTracksViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopTracksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: TopTrackItemBinding = DataBindingUtil.inflate(inflater, R.layout.top_track_item, parent, false)
        return TopTracksViewHolder(binding)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TopTracksViewHolder, position: Int) {
        holder.binding.track = tracks[position]
    }

    class TopTracksViewHolder(val binding: TopTrackItemBinding) : RecyclerView.ViewHolder(binding.root)
}