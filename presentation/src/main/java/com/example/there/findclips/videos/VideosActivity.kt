package com.example.there.findclips.videos

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.there.findclips.R
import com.example.there.findclips.util.app
import javax.inject.Inject

class VideosActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: VideosViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)

        app.createVideosComponent().inject(this)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(VideosViewModel::class.java)
        viewModel.getVideos(intent.getStringExtra(EXTRA_QUERY))
    }

    companion object {
        private const val EXTRA_QUERY = "EXTRA_QUERY"

        fun startingIntent(context: Context, query: String): Intent = Intent(context, VideosActivity::class.java).apply {
            putExtra(EXTRA_QUERY, query)
        }
    }
}
