package com.example.there.findclips.videos

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseVMActivity
import com.example.there.findclips.databinding.ActivityVideosBinding
import com.example.there.findclips.util.app
import javax.inject.Inject

class VideosActivity : BaseVMActivity<VideosViewModel>() {

    @Inject
    lateinit var viewModelFactory: VideosViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        if (savedInstanceState == null) {
            viewModel.getVideos(query = intent.getStringExtra(EXTRA_QUERY))
        }
    }

    private fun initBinding() {
        val binding: ActivityVideosBinding = DataBindingUtil.setContentView(this, R.layout.activity_videos)
        binding.viewState = viewModel.viewState
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VideosViewModel::class.java)
    }

    override fun initComponent() {
        app.createVideosComponent().inject(this)
    }

    override fun releaseComponent() {
        app.releaseVideosComponent()
    }

    companion object {
        private const val EXTRA_QUERY = "EXTRA_QUERY"

        fun startingIntent(context: Context, query: String): Intent = Intent(context, VideosActivity::class.java).apply {
            putExtra(EXTRA_QUERY, query)
        }
    }
}
