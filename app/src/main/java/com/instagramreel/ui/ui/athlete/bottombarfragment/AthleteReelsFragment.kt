package com.instagramreel.ui.ui.athlete.bottombarfragment

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.instagramreel.databinding.FragmentAthleteReelsBinding
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.callbacks.onBottomNavigationClickAthlete
import com.instagramreel.ui.model.reels.BodyItem
import com.instagramreel.ui.model.reels.ExoPlayerItem
import com.instagramreel.ui.model.reels.LikeOrDislikeRequest
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.sharedPref.AppPrefs
import com.instagramreel.ui.ui.athlete.activity.CameraActivity
import com.instagramreel.ui.ui.athlete.adapter.AthleteReelsAdapter
import com.instagramreel.ui.utils.Status
import com.instagramreel.ui.viewModelFactory.ViewModelFactory
import com.instagramreel.ui.viewmodel.bucketviewmodel.BucketDetailsViewModel
import com.instagramreel.ui.viewmodel.reels.GetReelsViewModel

class AthleteReelsFragment : Fragment() {

    private var reelsList: ArrayList<BodyItem> = ArrayList()
    lateinit var binding: FragmentAthleteReelsBinding
    var callback: onBottomNavigationClickAthlete? = null
    val exoPlayerItems = ArrayList<ExoPlayerItem>()

    private lateinit var viewModelFactory: ViewModelFactory
    private var getReelsViewModel: GetReelsViewModel? = null
    private var bucketDetailsViewModel: BucketDetailsViewModel? = null
    private val appPrefs: AppPrefs by lazy {
        AppPrefs(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAthleteReelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = ViewModelFactory(Application(), Repository())

        //Reels Details
        getReelsViewModel = ViewModelProvider(this, viewModelFactory).get(
            GetReelsViewModel::class.java
        )
        getReelsViewModel!!.getAllReels()

        //Bucket Details
        bucketDetailsViewModel = ViewModelProvider(this, viewModelFactory).get(
            BucketDetailsViewModel::class.java
        )
        bucketDetailsViewModel!!.getBucketDetails()

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

    private fun initUI() {
        onClick()
    }

    private fun reelsRV() {
        binding.reelsRV.adapter = AthleteReelsAdapter(this, reelsList, object : AthleteReelsAdapter.OnVideoPrepareListener{
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)
            }
        },object : AthleteReelsAdapter.onClickCallback{
            override fun onCLick(id: String, isLike: String) {
                if (isLike == "0") {
                    getReelsViewModel?.onLike(LikeOrDislikeRequest(id,"1"))
                    getReelsViewModel!!.getAllReels()
                }
                else if (isLike == "1") {
                    getReelsViewModel?.onLike(LikeOrDislikeRequest(id,"0"))
                    getReelsViewModel!!.getAllReels()
                }
            }
        })
    }

    private fun setObserver() {
        getReelsViewModel?.getReels?.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        reelsList = (it?.data?.body as ArrayList<BodyItem>?)!!
                        binding.shimmerCL.visibility = View.GONE
                        binding.clMain.visibility = View.VISIBLE
                        reelsRV()
                    }
                    Status.LOADING -> {
                        binding.shimmerFrameLayout.startShimmer()
                        //BaseActivity.showLoader(requireActivity())
                    }
                    Status.ERROR -> {
                        //BaseActivity.hideLoader()
                        binding.shimmerFrameLayout.startShimmer()
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            getReelsViewModel?.likeReels?.observe(viewLifecycleOwner) {
                it.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            appPrefs.getStringKey("token")
                            BaseActivity.hideLoader()
                            Toast.makeText(requireContext(),it.data?.message,Toast.LENGTH_SHORT).show()
                        }
                        Status.LOADING -> {
                            BaseActivity.showLoader(requireActivity())
                        }
                        Status.ERROR -> {
                            BaseActivity.hideLoader()
                            Toast.makeText(requireContext(),resource.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        bucketDetailsViewModel?.bucket?.observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        it?.data?.body
                        appPrefs.setString("Access_KEY",it?.data?.body?.Access_KEY.toString())
                        appPrefs.setString("Secret_Key",it?.data?.body?.Secret_Key.toString())
                        appPrefs.setString("Bucket_name",it?.data?.body?.Bucket_name.toString())
                    }
                    Status.LOADING -> {
                        binding.shimmerFrameLayout.startShimmer()
                        //BaseActivity.showLoader(requireActivity())
                    }
                    Status.ERROR -> {
                        //BaseActivity.hideLoader()
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
            btnCamera.setOnClickListener {
                startActivity(Intent(requireContext(), CameraActivity::class.java))
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as onBottomNavigationClickAthlete
    }
}