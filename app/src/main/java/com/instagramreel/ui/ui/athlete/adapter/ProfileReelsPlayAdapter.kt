package com.instagramreel.ui.ui.athlete.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.instagramreel.R
import com.instagramreel.databinding.ReelsItemBinding
import com.instagramreel.ui.model.athlete.getmydetails.Body
import com.instagramreel.ui.model.reels.ExoPlayerItem
import com.instagramreel.ui.ui.athlete.reels.ProfileReelsFragment

class ProfileReelsPlayAdapter(
    val context: ProfileReelsFragment,
    private val userData: Body?,
    private val videoPrepareListener: OnVideoPrepareListener
) : RecyclerView.Adapter<ProfileReelsPlayAdapter.ViewHolder>(), Player.Listener {

    class ViewHolder(val binding: ReelsItemBinding, val context: Context, private val videoPrepareListener: OnVideoPrepareListener) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var simpleExoPlayer: ExoPlayer
        private lateinit var mediaSource: MediaSource

        fun setVideoPath(url: String) {
            simpleExoPlayer = ExoPlayer.Builder(context).build()
            simpleExoPlayer.addListener(object : Player.Listener{
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    //Toast.makeText(context, "Can't play this video", Toast.LENGTH_SHORT).show()
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)

                    if (playbackState == Player.STATE_BUFFERING) {
                        binding.pbLoad.visibility = View.VISIBLE
                    }
                    else if (playbackState == Player.STATE_READY) {
                        binding.pbLoad.visibility = View.GONE
                    }
                }
            })

            binding.reelsPlayer.player = simpleExoPlayer

            simpleExoPlayer.seekTo(0)
            simpleExoPlayer.repeatMode = Player.REPEAT_MODE_ONE

            val dataSourceFactory = DefaultDataSource.Factory(context)
            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
            simpleExoPlayer.addMediaSource(mediaSource)
            simpleExoPlayer.prepare()

            if (absoluteAdapterPosition == 0) {
                simpleExoPlayer.playWhenReady = true
                simpleExoPlayer.play()
            }

            videoPrepareListener.onVideoPrepared(ExoPlayerItem(simpleExoPlayer,absoluteAdapterPosition))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReelsItemBinding.inflate(LayoutInflater.from(context.requireContext()),parent,false)
        return ViewHolder(binding, context.requireContext(),videoPrepareListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {

            if (userData?.profilePic == null) {
                Glide.with(context).load(R.drawable.profile_placeholder)
                    .into(profileIV)
            } else {
                Glide.with(context).load(userData.profilePic).placeholder(R.drawable.profile_placeholder)
                    .into(profileIV)
            }

            playerName.text = userData!!.fullname
            holder.setVideoPath(userData.userReeels!![position]?.videoUrl.toString())
        }
    }

    override fun getItemCount(): Int {
        return userData?.userReeels!!.size
    }

    interface OnVideoPrepareListener {
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
    }
}