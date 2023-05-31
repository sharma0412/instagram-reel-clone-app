package com.instagramreel.ui.ui.scout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.instagramreel.R
import com.instagramreel.databinding.AdapterExperienceBinding
import com.instagramreel.ui.model.scout.getmydetails.Body
import com.instagramreel.ui.ui.scout.bottombarfragment.ProfileFragment

class ProfileExperienceAdapter(val context: ProfileFragment, private val userData: Body?) : RecyclerView.Adapter<ProfileExperienceAdapter.ViewHolder>() {

    class ViewHolder(val binding : AdapterExperienceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<AdapterExperienceBinding>(LayoutInflater.from(parent.context), R.layout.adapter_experience,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvAt.text = userData?.usersexpertise?.get(position)?.jobTitle
            tvFrom.text = userData?.usersexpertise?.get(position)?.from
            tvTo.text = userData?.usersexpertise?.get(position)?.to
        }
    }

    override fun getItemCount(): Int {
        return userData?.usersexpertise?.size!!
    }
}