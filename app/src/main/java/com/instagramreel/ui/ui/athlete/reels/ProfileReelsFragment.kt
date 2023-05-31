package com.instagramreel.ui.ui.athlete.reels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.instagramreel.databinding.FragmentProfileReelsBinding
import com.instagramreel.ui.model.athlete.getmydetails.Body
import com.instagramreel.ui.model.reels.ExoPlayerItem
import com.instagramreel.ui.ui.athlete.adapter.ProfileReelsPlayAdapter

class ProfileReelsFragment : Fragment() {

    private var selectedPosition: Int? = null
    private var userData: Body? = null
    lateinit var binding: FragmentProfileReelsBinding
    val exoPlayerItems = ArrayList<ExoPlayerItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileReelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userData = arguments?.getParcelable("reelsModel") as? Body
        selectedPosition = arguments?.getInt("reelsPosition")
        initUi()

        binding.reelsRV.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer.isPlaying }
                if (previousIndex != -1) {
                    val player = exoPlayerItems[previousIndex].exoPlayer
                    player.pause()
                    player.playWhenReady = false
                }

                val newIndex = exoPlayerItems.indexOfFirst { it.position == position }
                if (newIndex != -1) {
                    val player = exoPlayerItems[newIndex].exoPlayer
                    player.playWhenReady = true
                    player.play()
                }
            }
        })
    }

    private fun initUi() {
        reelsRV()
    }

    override fun onPause() {
        super.onPause()

        val index = exoPlayerItems.indexOfFirst { it.position == binding.reelsRV.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()
        val index = exoPlayerItems.indexOfFirst { it.position == binding.reelsRV.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.playWhenReady = true
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (exoPlayerItems.isNotEmpty()) {
            for (item in exoPlayerItems) {
                val player = item.exoPlayer
                player.stop()
                player.clearMediaItems()
            }
        }
    }

    private fun reelsRV() {
        binding.apply {
            reelsRV.post { reelsRV.currentItem = selectedPosition!! }
            reelsRV.adapter = ProfileReelsPlayAdapter(
                this@ProfileReelsFragment,
                userData,
                object : ProfileReelsPlayAdapter.OnVideoPrepareListener {
                    override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                        exoPlayerItems.add(exoPlayerItem)
                    }
                })
        }
    }
}