package com.instagramreel.ui.ui.scout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.instagramreel.databinding.ExperienceAdapterBinding
import com.instagramreel.ui.model.ExperienceModel
import com.instagramreel.ui.ui.scout.fragment.signup.MyDetailFragment
import java.util.*


class ExperienceAdapter(
    var context: MyDetailFragment,
    private val experienceList: ArrayList<ExperienceModel>
) : RecyclerView.Adapter<ExperienceAdapter.ViewHolder>() {

    class ViewHolder(val binding: ExperienceAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ExperienceAdapterBinding>(
            LayoutInflater.from(parent.context),
            com.instagramreel.R.layout.experience_adapter,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (experienceList.size == 0) {
            Toast.makeText(context.requireContext(), "At least one experience is required.", Toast.LENGTH_SHORT).show()
        }
        else {
            holder.binding.experienceETT.text = experienceList[position].job_title
            holder.binding.tvFromDate.text = experienceList[position].from
            holder.binding.tvToDate.text = experienceList[position].to
            holder.binding.spnSpecialized.text = experienceList[position].itemName
        }

        holder.binding.apply {
            btnRemove.setOnClickListener {
                experienceList.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return experienceList.size
    }
}