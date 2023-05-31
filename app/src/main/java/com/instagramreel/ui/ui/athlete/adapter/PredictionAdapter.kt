package com.instagramreel.ui.ui.athlete.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.instagramreel.databinding.ItemPredictionBinding

class PredictionAdapter(private val context:Context?,
                        private var list:ArrayList<AutocompletePrediction>,
                        private var mCallback : PredictionCallbacks
    ) : RecyclerView.Adapter<PredictionAdapter.PredictionViewHolder>() {

    inner class PredictionViewHolder(val binding: ItemPredictionBinding) : RecyclerView.ViewHolder(binding.root) {

        init{
            binding.root.setOnClickListener {
                mCallback.onClickAddressItem(list[bindingAdapterPosition])
            }
        }

        fun bind(data : AutocompletePrediction){
            binding.spinnerFilterName.text = data.getFullText(null).toString()
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionViewHolder {
        val binding = ItemPredictionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PredictionViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PredictionViewHolder, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list:ArrayList<AutocompletePrediction>){
        this.list = ArrayList()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    interface PredictionCallbacks{
        fun onClickAddressItem(data : AutocompletePrediction)
    }

}