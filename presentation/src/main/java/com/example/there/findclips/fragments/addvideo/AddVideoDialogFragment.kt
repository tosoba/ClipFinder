package com.example.there.findclips.fragments.addvideo

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.activities.player.PlayerActivity
import com.example.there.findclips.databinding.DialogAddVideoBinding
import com.example.there.findclips.model.entities.VideoPlaylist
import com.example.there.findclips.view.lists.OnVideoPlaylistClickListener
import com.example.there.findclips.view.lists.VideoPlaylistsList
import com.example.there.findclips.view.recycler.SeparatorDecoration

class AddVideoDialogFragment : DialogFragment() {
    lateinit var state: AddVideoViewState

    private val playerActivity: PlayerActivity?
        get() = activity as? PlayerActivity

    private val onVideoPlaylistSelectedListener = object : OnVideoPlaylistClickListener {
        override fun onClick(item: VideoPlaylist) {
            playerActivity?.addVideoToPlaylist(playlist = item)
            dismiss()
        }
    }

    private val onAddNewPlaylistsBtnClickListener = View.OnClickListener {
        playerActivity?.showNewPlaylistDialog()
    }

    private val view: AddVideoView by lazy {
        AddVideoView(state = state,
                playlistsAdapter = VideoPlaylistsList.Adapter(state.playlists, R.layout.video_playlist_item, onVideoPlaylistSelectedListener),
                itemDecoration = SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f),
                onAddNewPlaylistBtnClickListener = onAddNewPlaylistsBtnClickListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: DialogAddVideoBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_video, container, false)
        binding.view = view
        binding.addVideoPlaylistsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}