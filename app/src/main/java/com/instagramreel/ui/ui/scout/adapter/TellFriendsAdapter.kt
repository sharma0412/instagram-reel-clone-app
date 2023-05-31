package com.instagramreel.ui.ui.scout.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.instagramreel.BuildConfig
import com.instagramreel.R
import com.instagramreel.databinding.AdapterTellfriendBinding
import com.instagramreel.ui.model.contacts.ContactModel
import com.instagramreel.ui.ui.scout.settings.TellFriendFragment

class TellFriendsAdapter (val context: TellFriendFragment,
private val contactList: ArrayList<ContactModel>
) : RecyclerView.Adapter<TellFriendsAdapter.ViewHolder>() {

    class ViewHolder(val binding: AdapterTellfriendBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<AdapterTellfriendBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_tellfriend,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {

            name.text = contactList[position].name
            tvNumber.text = contactList[position].number

            Glide.with(context).load(R.drawable.profile_placeholder).into(profileIV)

            container.setOnClickListener {
                val smsUri: Uri = Uri.parse("smsto:" + contactList[position].number)
                val smsIntent = Intent(Intent.ACTION_SENDTO, smsUri)
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    """
                        ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                        
                        
                        """.trimIndent()
                smsIntent.putExtra("sms_body", shareMessage)
                ContextCompat.startActivity(context.requireContext(), smsIntent, null)
            }
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}