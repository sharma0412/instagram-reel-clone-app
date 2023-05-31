package com.instagramreel.ui.ui.scout.bottombarfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.instagramreel.databinding.FragmentReelsBinding
import com.instagramreel.ui.ui.scout.adapter.DiscoverReelsAdapter

class DiscoverReelsFragment : Fragment() {

    lateinit var binding: FragmentReelsBinding

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
        initUI()
    }

    private fun initUI() {
        onClick()
        reelsRV()
    }

    private fun reelsRV() {
        binding.reelsRV.adapter = DiscoverReelsAdapter(this)
    }

    private fun onClick() {
        binding.apply {

        }
    }
}