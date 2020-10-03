package com.example.soundcloudplayer

import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.core.ext.castAs
import com.example.core.android.base.fragment.ISoundCloudPlayerFragment
import com.example.core.android.base.handler.SlidingPanelController
import com.example.core.android.model.soundcloud.SoundCloudTrack
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_sound_cloud_player.*
import kotlinx.android.synthetic.main.fragment_sound_cloud_player.view.*

class SoundCloudPlayerFragment : Fragment(), ISoundCloudPlayerFragment {

    private val exoPlayer: SimpleExoPlayer by lazy(LazyThreadSafetyMode.NONE) {
        ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector()).apply {
            playWhenReady = true
        }
    }

    private var lastTrack: SoundCloudTrack? = null

    override val playerView: View?
        get() = this.view

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sound_cloud_player, container, false).apply {
        close_sound_cloud_player_image_button.setOnClickListener {
            activity?.castAs<SlidingPanelController>()?.hideIfVisible()
            exoPlayer.stop()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.sound_cloud_exo_player_view?.player = exoPlayer
    }

    override fun onDestroy() {
        exoPlayer.stop()
        exoPlayer.release()
        super.onDestroy()
    }

    override val lastPlayedTrack: SoundCloudTrack? get() = lastTrack

    override fun loadTrack(track: SoundCloudTrack) {
        lastTrack = track

        Picasso.with(context)
            .load(track.artworkUrl)
            .placeholder(com.example.core.android.R.drawable.track_placeholder)
            .error(com.example.core.android.R.drawable.track_placeholder)
            .into(sound_cloud_current_track_image_view)

        val userAgent = Util.getUserAgent(context, getString(com.example.core.android.R.string.app_name))
        val mediaUri = Uri.parse(track.streamUrl)
        val mediaSource = ExtractorMediaSource(
            mediaUri,
            DefaultDataSourceFactory(context, userAgent),
            DefaultExtractorsFactory(),
            null,
            null
        )

        exoPlayer.prepare(mediaSource)

        context?.let {
            val componentName = ComponentName(it, "Exo")
            val mediaSession = MediaSessionCompat(it, "ExoPlayer", componentName, null)
            val playbackStateBuilder = PlaybackStateCompat.Builder()
            playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE)
            mediaSession.setPlaybackState(playbackStateBuilder.build())
            mediaSession.isActive = true
        }
    }

    override fun onHidden() = exoPlayer.stop()

    override fun stopPlayback() = exoPlayer.stop()
}
