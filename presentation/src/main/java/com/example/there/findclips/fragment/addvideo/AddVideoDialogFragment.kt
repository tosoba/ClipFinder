package com.example.there.findclips.fragment.addvideo

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.databinding.DialogAddVideoBinding
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.example.there.findclips.view.recycler.SeparatorDecoration

class AddVideoDialogFragment : DialogFragment() {

    lateinit var state: AddVideoViewState

    private val onAddNewPlaylistsBtnClickListener = View.OnClickListener {
        mainActivity?.showNewPlaylistDialog()
    }

    private val view: AddVideoView by lazy {
        AddVideoView(
                state = state,
                playlistsRecyclerViewItemView = RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                ObservableField(false),
                                state.playlists
                        ),
                        object : ListItemView<VideoPlaylist>(state.playlists) {
                            override val itemViewBinder: ItemBinder<VideoPlaylist>
                                get() = ItemBinderBase(BR.playlist, R.layout.video_playlist_item)
                        },
                        ClickHandler {
                            mainActivity?.addVideoToPlaylist(playlist = it)
                            dismiss()
                        },
                        SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorPrimary, null), 2f),
                        null
                ),
                onAddNewPlaylistBtnClickListener = onAddNewPlaylistsBtnClickListener
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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