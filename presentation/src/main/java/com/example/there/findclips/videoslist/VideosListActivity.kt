package com.example.there.findclips.videoslist

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MenuItem
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseVMActivity
import com.example.there.findclips.databinding.ActivityVideosBinding
import com.example.there.findclips.util.app
import com.example.there.findclips.videossearch.VideosSearchViewModel
import com.example.there.findclips.videossearch.VideosSearchVMFactory
import javax.inject.Inject

class VideosListActivity : BaseVMActivity<VideosSearchViewModel>() {

    @Inject
    lateinit var searchVMFactory: VideosSearchVMFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        setUpDisplayHome()

        if (savedInstanceState == null) {
            viewModel.getVideos(query = intent.getStringExtra(EXTRA_QUERY))
        }
    }

    private fun initBinding() {
        val binding: ActivityVideosBinding = DataBindingUtil.setContentView(this, R.layout.activity_videos)
        binding.viewState = viewModel.viewState
    }

    private fun setUpDisplayHome() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, searchVMFactory).get(VideosSearchViewModel::class.java)
    }

    override fun initComponent() {
        app.createVideosComponent().inject(this)
    }

    override fun releaseComponent() {
        app.releaseVideosComponent()
    }

    companion object {
        private const val EXTRA_QUERY = "EXTRA_QUERY"

        fun startingIntent(context: Context, query: String): Intent = Intent(context, VideosListActivity::class.java).apply {
            putExtra(EXTRA_QUERY, query)
        }
    }
}
