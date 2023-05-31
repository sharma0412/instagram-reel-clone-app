package com.instagramreel.ui.ui.athlete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.instagramreel.R
import com.instagramreel.databinding.KickoffAdapterBinding
import com.instagramreel.ui.model.athlete.kickoff.KickOffResponse
import com.instagramreel.ui.ui.athlete.bottombarfragment.KickoffFragment
import com.instagramreel.ui.utils.getDateWithWeekName

class KickoffGameAdapter(
    val context: KickoffFragment,
    private val kickOffList: ArrayList<KickOffResponse.Body>,
    val callback: KickoffCallback
) : RecyclerView.Adapter<KickoffGameAdapter.ViewHolder>() {

    class ViewHolder(val binding: KickoffAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<KickoffAdapterBinding>(LayoutInflater.from(parent.context), R.layout.kickoff_adapter,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            cvGame.setOnClickListener {
                callback.onClick(kickOffList,position)
            }

            Glide.with(context).load(kickOffList[position].game_image).into(ivGame)
            tvGameStatus.text = kickOffList[position].game_name
            tvGameDay.text = getDateWithWeekName(kickOffList[position].date)
            tvGameName.text = kickOffList[position].kicksport.name
        }
    }

    override fun getItemCount(): Int {
        return kickOffList.size
    }

    interface KickoffCallback {
        fun onClick(kickOffList: ArrayList<KickOffResponse.Body> ,position: Int)
    }
}