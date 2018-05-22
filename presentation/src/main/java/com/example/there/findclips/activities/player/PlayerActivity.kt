package com.example.there.findclips.activities.player

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.there.findclips.Keys
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseVMActivity
import com.example.there.findclips.databinding.ActivityPlayerBinding
import com.example.there.findclips.model.entities.Video
import com.example.there.findclips.util.app
import com.example.there.findclips.util.screenOrientation
import com.example.there.findclips.view.OnTabSelectedListener
import com.example.there.findclips.view.lists.OnVideoClickListener
import com.example.there.findclips.view.lists.RelatedVideosList
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener
import com.example.there.findclips.view.recycler.SeparatorDecoration
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.activity_player.*
import javax.inject.Inject


class PlayerActivity : BaseVMActivity<PlayerViewModel>(), YouTubePlayer.OnInitializedListener {

    private val intentVideo: Video by lazy { intent.getParcelableExtra<Video>(EXTRA_VIDEO) }
    private val intentOtherVideos: ArrayList<Video> by lazy { intent.getParcelableArrayListExtra<Video>(EXTRA_OTHER_VIDEOS) }

    private val onVideoItemClickListener = object : OnVideoClickListener {
        override fun onClick(item: Video) {
            player?.loadVideo(item.id)
        }
    }

    private val onTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) = viewModel.viewState.currentTabPosition.set(tab?.position ?: 0)
    }

    private val onFavouriteBtnClickListener = View.OnClickListener {

    }

    private val onRelatedVideosListScrollListener = object : EndlessRecyclerOnScrollListener() {
        override fun onLoadMore() = viewModel.searchRelatedVideosWithToLastId()
    }

    private val view: PlayerView by lazy {
        PlayerView(state = viewModel.viewState,
                relatedVideosAdapter = RelatedVideosList.Adapter(viewModel.viewState.videos, R.layout.related_video_item, onVideoItemClickListener),
                otherVideosAdapter = RelatedVideosList.Adapter(ObservableArrayList<Video>().apply {
                    addAll(intentOtherVideos)
                }, R.layout.related_video_item, onVideoItemClickListener),
                relatedVideosItemDecoration = SeparatorDecoration(this@PlayerActivity, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f),
                otherVideosItemDecoration = SeparatorDecoration(this@PlayerActivity, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f),
                onRelatedVideosScrollListener = onRelatedVideosListScrollListener,
                onTabSelectedListener = onTabSelectedListener,
                onFavouriteBtnClickListener = onFavouriteBtnClickListener
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        viewModel.searchRelatedVideos(intentVideo.id)
    }

    private val playerFragment: YouTubePlayerSupportFragment?
        get() = supportFragmentManager.findFragmentById(R.id.player_fragment) as? YouTubePlayerSupportFragment

    private fun initView() {
        val binding: ActivityPlayerBinding = DataBindingUtil.setContentView(this, R.layout.activity_player)
        binding.view = view
        binding.playerRelatedVideosRecyclerView.layoutManager = LinearLayoutManager(this@PlayerActivity, LinearLayoutManager.VERTICAL, false)
        binding.playerOtherVideoResultsRecyclerView.layoutManager = LinearLayoutManager(this@PlayerActivity, LinearLayoutManager.VERTICAL, false)

        if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            player_favourite_video_fab?.visibility = View.GONE
        }

        playerFragment?.initialize(Keys.youtubeApi, this)
    }

    private var player: YouTubePlayer? = null

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
        this.player = player
        player?.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT)
        player?.loadVideo(intentVideo.id)
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, errorReason: YouTubeInitializationResult) {
        if (errorReason.isUserRecoverableError) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
        } else {
            val errorMessage = String.format(getString(R.string.error_player), errorReason.toString())
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            playerFragment?.initialize(Keys.youtubeApi, this)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            player_tab_layout?.visibility = View.GONE
            player_favourite_video_fab?.visibility = View.GONE
        } else {
            player_tab_layout?.visibility = View.VISIBLE
            player_favourite_video_fab?.visibility = View.VISIBLE
        }
    }

    override fun initComponent() = app.createPlayerSubComponent().inject(this)

    override fun releaseComponent() = app.releasePlayerSubComponent()

    @Inject
    lateinit var factory: PlayerVMFactory

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(PlayerViewModel::class.java)
    }

    companion object {
        private const val RECOVERY_DIALOG_REQUEST = 100

        private const val EXTRA_VIDEO = "EXTRA_VIDEO"
        private const val EXTRA_OTHER_VIDEOS = "EXTRA_OTHER_VIDEOS"

        fun start(activity: Activity, video: Video, otherVideos: ArrayList<Video>) {
            val intent = Intent(activity, PlayerActivity::class.java).apply {
                putExtra(EXTRA_VIDEO, video)
                putParcelableArrayListExtra(EXTRA_OTHER_VIDEOS, otherVideos)
            }
            activity.startActivity(intent)
        }
    }
}
