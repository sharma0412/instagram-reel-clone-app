package com.instagramreel.ui.ui.scout.bottombarfragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.instagramreel.databinding.FragmentReelsBinding
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.callbacks.onBottomNavigationClickAthlete
import com.instagramreel.ui.model.reels.BodyItem
import com.instagramreel.ui.model.reels.ExoPlayerItem
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.ui.scout.adapter.ReelsAdapter
import com.instagramreel.ui.utils.Status
import com.instagramreel.ui.viewModelFactory.ViewModelFactory
import com.instagramreel.ui.viewmodel.reels.GetReelsViewModel

class ReelsFragment : Fragment() {

    private var reelsList: ArrayList<BodyItem> = ArrayList()
    var callback: onBottomNavigationClickAthlete? = null
    lateinit var binding: FragmentReelsBinding
    val exoPlayerItems = ArrayList<ExoPlayerItem>()

    private lateinit var viewModelFactory: ViewModelFactory
    private var getReelsViewModel: GetReelsViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReelsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = ViewModelFactory(Application(), Repository())
        getReelsViewModel = ViewModelProvider(this, viewModelFactory).get(
            GetReelsViewModel::class.java
        )
        getReelsViewModel!!.getAllReels()
        setObserver()
        initUI()

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

    private fun initUI() {
        onClick()
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
        binding.reelsRV.adapter = ReelsAdapter(this, reelsList, object : ReelsAdapter.OnVideoPrepareListener{
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)
            }
        })
    }

    private fun setObserver() {
        getReelsViewModel?.getReels?.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        binding.shimmerCL.visibility = View.GONE
                        binding.clMain.visibility = View.VISIBLE
                        reelsList = (it?.data?.body as ArrayList<BodyItem>?)!!
                        reelsRV()
                    }
                    Status.LOADING -> {
                        binding.shimmerFrameLayout.startShimmer()
                    }
                    Status.ERROR -> {
                        binding.shimmerFrameLayout.startShimmer()
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun onClick() {
        binding.apply {

        }
    }
}