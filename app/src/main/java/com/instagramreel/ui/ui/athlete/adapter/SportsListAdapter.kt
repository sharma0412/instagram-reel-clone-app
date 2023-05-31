package com.instagramreel.ui.ui.athlete.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.instagramreel.R
import com.instagramreel.databinding.SpinnerSelectionBinding
import com.instagramreel.ui.ui.athlete.fragments.AthleteUpdateDetailsFragment
import com.instagramreel.ui.model.scout.getsupportsdata.GetSportsResponse

class SportsListAdapter(val context: AthleteUpdateDetailsFragment, private val sportsList: ArrayList<GetSportsResponse.Body>, val callback: SportsCallback) : RecyclerView.Adapter<SportsListAdapter.ViewHolder>() {

    class ViewHolder(val binding: SpinnerSelectionBinding) : RecyclerView.ViewHolder(binding.root)
    val list = ArrayList<String>()
    private val idList = ArrayList<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<SpinnerSelectionBinding>(
            LayoutInflater.from(parent.context),
            R.layout.spinner_selection,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.apply {
            sbList.text = sportsList[position].name

            sbList.setOnCheckedChangeListener { _, p1 ->
                if (p1) {
                    list.add(sportsList[position].name)
                    idList.add(sportsList[position].id)
                    callback.onClick(idList, list)
                }
                else if (!p1){
                    list.remove(sportsList[position].name)
                    idList.remove(sportsList[position].id)
                    callback.onClick(idList, list)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return sportsList.size
    }

    interface SportsCallback {
        fun onClick(id : List<Int>,name: List<String>)
    }
}