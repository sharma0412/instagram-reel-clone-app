package com.instagramreel.ui.ui.scout.adapter

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.instagramreel.R
import com.instagramreel.databinding.ReelsItemBinding
import com.instagramreel.ui.ui.scout.bottombarfragment.DiscoverReelsFragment

class DiscoverReelsAdapter(var context: DiscoverReelsFragment) :
    RecyclerView.Adapter<DiscoverReelsAdapter.ViewHolder>() {
    private val mediaPlayer: MediaPlayer = MediaPlayer()

    class ViewHolder(val binding: ReelsItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ReelsItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.reels_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            /*playerName.text = videoList[position].Title
            reelsPlayer.setVideoPath(videoList[position].URL).toString()*/
            /*reelsPlayer.setOnPreparedListener {
                mediaPlayer.start()
                val videoRatio = mediaPlayer.videoWidth / mediaPlayer.videoHeight.toFloat()
                val screenRatio = reelsPlayer.width / reelsPlayer.height.toFloat()
                val scale = videoRatio / screenRatio
                if (scale >= 1f) {
                    reelsPlayer.scaleX = scale
                } else {
                    //reelsPlayer.scaleY = 1f / scale
                }
            }

            reelsPlayer.setOnCompletionListener {
                mediaPlayer.start()
            }*/
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}