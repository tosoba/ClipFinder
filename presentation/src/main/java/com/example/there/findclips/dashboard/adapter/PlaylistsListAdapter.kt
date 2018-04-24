package com.example.there.findclips.dashboard.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.domain.entities.PlaylistEntity
import com.example.there.findclips.R
import com.example.there.findclips.databinding.PlaylistItemBinding

class PlaylistsListAdapter(private val playlists: List<PlaylistEntity>) : RecyclerView.Adapter<PlaylistsListAdapter.PlaylistsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: PlaylistItemBinding = DataBindingUtil.inflate(inflater, R.layout.playlist_item, parent, false)
        return PlaylistsViewHolder(binding)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.binding.playlist = playlists[position]
    }

    class PlaylistsViewHolder(val binding: PlaylistItemBinding) : RecyclerView.ViewHolder(binding.root)
}