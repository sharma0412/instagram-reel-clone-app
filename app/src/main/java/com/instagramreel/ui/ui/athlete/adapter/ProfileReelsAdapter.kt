package com.instagramreel.ui.ui.athlete.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.instagramreel.R
import com.instagramreel.databinding.ReelsAdapterBinding
import com.instagramreel.ui.model.athlete.getmydetails.Body
import com.instagramreel.ui.ui.athlete.bottombarfragment.AthleteProfileFragment

class ProfileReelsAdapter(
    val context: AthleteProfileFragment,
    private val userData: Body?,
) : RecyclerView.Adapter<ProfileReelsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ReelsAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ReelsAdapterBinding>(
            LayoutInflater.from(parent.context),
            R.layout.reels_adapter,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if (userData?.userReeels!![position]?.videoThumbnail.isNullOrEmpty()) {
                Glide.with(context).load(userData.userReeels[position]?.videoUrl).into(reels)
            }
            else {
                Glide.with(context).load(userData.userReeels[position]?.videoThumbnail).into(reels)
            }

            root.setOnClickListener {
                val bundle =  Bundle()
                bundle.putParcelable("reelsModel",userData)
                bundle.putInt("reelsPosition",position)
                context.findNavController().navigate(R.id.action_athleteProfileFragment_to_profileReelsFragment,bundle)
            }
        }
    }

    override fun getItemCount(): Int {
        return userData?.userReeels!!.size
    }
}