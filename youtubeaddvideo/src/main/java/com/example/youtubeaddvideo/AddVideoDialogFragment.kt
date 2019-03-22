package com.example.youtubeaddvideo

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coreandroid.model.videos.VideoPlaylist
import com.example.coreandroid.util.ext.videoPlaylistController
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.decoration.SeparatorDecoration
import com.example.coreandroid.view.recyclerview.item.ListItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemViewState
import com.example.coreandroid.view.recyclerview.listener.ClickHandler
import com.example.coreandroid.BR
import com.example.youtubeaddvideo.databinding.DialogAddVideoBinding


class AddVideoDialogFragment : DialogFragment() {

    lateinit var state: AddVideoViewState

    private val onAddNewPlaylistsBtnClickListener = View.OnClickListener {
        videoPlaylistController?.showNewPlaylistDialog()
    }

    private val view: AddVideoView by lazy {
        AddVideoView(
                state = state,
                playlistsRecyclerViewItemView = RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                ObservableField(false),
                                state.playlists,
                                ObservableField(false)
                        ),
                        object : ListItemView<VideoPlaylist>(state.playlists) {
                            override val itemViewBinder: ItemBinder<VideoPlaylist>
                                get() = ItemBinderBase(BR.playlist, R.layout.video_playlist_item)
                        },
                        ClickHandler {
                            videoPlaylistController?.addVideoToPlaylist(playlist = it)
                            dismiss()
                        },
                        SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorPrimary, null), 2f),
                        null
                ),
                onAddNewPlaylistBtnClickListener = onAddNewPlaylistsBtnClickListener
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val binding: DialogAddVideoBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_video, container, false)
        binding.view = view
        binding.addVideoPlaylistsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}