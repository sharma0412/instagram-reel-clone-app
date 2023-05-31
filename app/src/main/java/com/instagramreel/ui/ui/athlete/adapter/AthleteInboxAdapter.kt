package com.instagramreel.ui.ui.athlete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.instagramreel.R
import com.instagramreel.databinding.AdapterInboxBinding
import com.instagramreel.ui.ui.athlete.bottombarfragment.AthleteInboxFragment

class AthleteInboxAdapter(val context: AthleteInboxFragment,val callback: AthleteInboxCallback) : RecyclerView.Adapter<AthleteInboxAdapter.ViewHolder>() {

    class ViewHolder(val binding : AdapterInboxBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<AdapterInboxBinding>(LayoutInflater.from(parent.context), R.layout.adapter_inbox,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            msgContainer.setOnClickListener {
                callback.onClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    interface AthleteInboxCallback {
        fun onClick(position: Int)
    }
}