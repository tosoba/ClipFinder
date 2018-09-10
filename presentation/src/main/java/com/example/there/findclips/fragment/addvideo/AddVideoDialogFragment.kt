package com.example.there.findclips.fragment.addvideo

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.databinding.DialogAddVideoBinding
import com.example.there.findclips.lifecycle.DisposablesComponent
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.view.list.impl.VideoPlaylistsList
import com.example.there.findclips.view.recycler.SeparatorDecoration

class AddVideoDialogFragment : DialogFragment() {

    lateinit var state: AddVideoViewState

    private val onAddNewPlaylistsBtnClickListener = View.OnClickListener {
        mainActivity?.showNewPlaylistDialog()
    }

    private val playlistsAdapter: VideoPlaylistsList.Adapter by lazy {
        VideoPlaylistsList.Adapter(state.playlists, R.layout.video_playlist_item)
    }

    private val view: AddVideoView by lazy {
        AddVideoView(
                state = state,
                playlistsAdapter = playlistsAdapter,
                itemDecoration = SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f),
                onAddNewPlaylistBtnClickListener = onAddNewPlaylistsBtnClickListener
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: DialogAddVideoBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_video, container, false)
        binding.view = view
        binding.addVideoPlaylistsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(disposablesComponent)
        disposablesComponent.add(playlistsAdapter.itemClicked.subscribe {
            mainActivity?.addVideoToPlaylist(playlist = it)
            dismiss()
        })
    }

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}