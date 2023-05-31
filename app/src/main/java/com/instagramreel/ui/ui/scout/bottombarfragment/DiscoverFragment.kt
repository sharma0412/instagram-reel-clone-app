package com.instagramreel.ui.ui.scout.bottombarfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.instagramreel.databinding.FragmentDiscoverBinding
import com.instagramreel.ui.callbacks.onBottomNavigationScout
import com.instagramreel.ui.ui.scout.adapter.RecommendedVideoAdapter

class DiscoverFragment : Fragment() {

    lateinit var binding: FragmentDiscoverBinding
    var callback: onBottomNavigationScout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoverBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        onClick()
        recommendedRV()
    }

    private fun recommendedRV() {
        binding.rvRecommended.adapter = RecommendedVideoAdapter(this,object: RecommendedVideoAdapter.RecommendedCallback{
            override fun onClick(position: Int) {
                callback?.getDiscoverReels()
            }

        })
        binding.rvFootball.adapter = RecommendedVideoAdapter(this,object: RecommendedVideoAdapter.RecommendedCallback{
            override fun onClick(position: Int) {
                callback?.getDiscoverReels()
            }

        })
        binding.rvSoccer.adapter = RecommendedVideoAdapter(this,object: RecommendedVideoAdapter.RecommendedCallback{
            override fun onClick(position: Int) {
                callback?.getDiscoverReels()
            }

        })
        binding.rvTennis.adapter = RecommendedVideoAdapter(this,object: RecommendedVideoAdapter.RecommendedCallback{
            override fun onClick(position: Int) {
                callback?.getDiscoverReels()
            }

        })
    }

    private fun onClick() {
        binding.apply {
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as onBottomNavigationScout
    }
}