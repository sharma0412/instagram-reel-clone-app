package com.instagramreel.ui.ui.athlete.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.RequestBuilder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.Util
import com.instagramreel.databinding.ActivityVideoPlayerBinding
import com.instagramreel.ui.sharedPref.AppPrefs

class VideoPlayerActivity : AppCompatActivity(), View.OnClickListener,Player.Listener{

    private lateinit var videoThumbnail: RequestBuilder<Drawable>
    private var uri: Uri? = null
    private lateinit var simpleExoPlayer: ExoPlayer
    private var streamURL = ""
    lateinit var binding: ActivityVideoPlayerBinding
    private val appPrefs: AppPrefs by lazy {
        AppPrefs(this@VideoPlayerActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        streamURL = appPrefs.getStringKey("videoPath").toString()
        uri = Uri.parse(streamURL)
        initializePlayer()

        binding.btCancel.setOnClickListener(this@VideoPlayerActivity)
        binding.btNext.setOnClickListener(this@VideoPlayerActivity)
    }


    private fun initializePlayer() {

        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(this)

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri!!))

        val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

        simpleExoPlayer = ExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        binding.playerView.player = simpleExoPlayer

        simpleExoPlayer.addMediaSource(mediaSource)
        simpleExoPlayer.prepare()

        binding.playerView.setShowNextButton(false)
        binding.playerView.setShowShuffleButton(false)
        binding.playerView.setShowPreviousButton(false)
        binding.playerView.setShowFastForwardButton(false)
        binding.playerView.setShowPreviousButton(false)
        binding.playerView.setShowRewindButton(false)

        simpleExoPlayer.playWhenReady = true

        simpleExoPlayer.addListener(this)
    }

    private fun releasePlayer() {
        simpleExoPlayer.release()
    }

    public override fun onStart() {
        super.onStart()

        if (Util.SDK_INT > 23) initializePlayer()
    }

    public override fun onResume() {
        super.onResume()

        if (Util.SDK_INT <= 23) initializePlayer()
    }

    public override fun onPause() {
        super.onPause()

        if (Util.SDK_INT <= 23) releasePlayer()
    }

    public override fun onStop() {
        super.onStop()

        if (Util.SDK_INT > 23) releasePlayer()
    }

    override fun onClick(p0: View?) {
        binding.apply {
            when(p0) {
                btCancel -> {
                    startActivity(Intent(this@VideoPlayerActivity,CameraActivity::class.java))
                    finish()
                }
                btNext -> {
                    startActivity(Intent(this@VideoPlayerActivity,PostVideoActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(this@VideoPlayerActivity, "error", Toast.LENGTH_SHORT).show()
        Log.e("TAG", "onPlayerError: ${error.toString()}")
    }
}