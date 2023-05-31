package com.instagramreel.ui.ui.scout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.instagramreel.R
import com.instagramreel.databinding.AdapterInboxBinding
import com.instagramreel.ui.ui.scout.bottombarfragment.InboxFragment

class InboxAdapter(val context: InboxFragment) : RecyclerView.Adapter<InboxAdapter.ViewHolder>() {

    class ViewHolder(val binding : AdapterInboxBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<AdapterInboxBinding>(LayoutInflater.from(parent.context), R.layout.adapter_inbox,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            msgContainer.setOnClickListener {
                context.findNavController().navigate(R.id.action_inboxFragment_to_chatFragment)
            }
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}