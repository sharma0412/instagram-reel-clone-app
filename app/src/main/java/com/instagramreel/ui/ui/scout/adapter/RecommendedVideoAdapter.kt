package com.instagramreel.ui.ui.scout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.instagramreel.R
import com.instagramreel.databinding.AdapterDiscoverBinding
import com.instagramreel.ui.ui.scout.bottombarfragment.DiscoverFragment

class RecommendedVideoAdapter(val context : DiscoverFragment,val callback: RecommendedCallback) : RecyclerView.Adapter<RecommendedVideoAdapter.ViewHolder>() {

    class ViewHolder(val binding : AdapterDiscoverBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<AdapterDiscoverBinding>(LayoutInflater.from(parent.context), R.layout.adapter_discover,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            reels.setOnClickListener {
                //context.findNavController().navigate(R.id.discoverReelsFragment)
                callback.onClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return 10
    }

    interface RecommendedCallback {
        fun onClick(position: Int)
    }
}