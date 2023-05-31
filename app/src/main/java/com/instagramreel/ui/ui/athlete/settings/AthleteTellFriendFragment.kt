package com.instagramreel.ui.ui.athlete.settings

import android.Manifest
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.instagramreel.databinding.FragmentTellFriendBinding
import com.instagramreel.ui.model.contacts.ContactModel
import com.instagramreel.ui.ui.athlete.adapter.AthleteTellFriendsAdapter

class AthleteTellFriendFragment : Fragment() {

    lateinit var binding: FragmentTellFriendBinding
    private val contactList: ArrayList<ContactModel> = ArrayList()
    private var cursor: Cursor? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTellFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readContacts()
        initUI()
    }

    private fun readContacts() {
        Dexter.withContext(requireContext()).withPermission(Manifest.permission.READ_CONTACTS).withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                if (response!!.permissionName.equals(Manifest.permission.READ_CONTACTS)) {
                    displayContacts()
                }
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    requireContext(),
                    "Permission should be granted!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }
        }).check()
    }

    private fun displayContacts() {

         cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
             "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC"
        )
        while (cursor?.moveToNext()!!) {
            val name = cursor!!.getString(cursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number = cursor!!.getString(cursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            contactList.add(ContactModel(name,number))
            binding.rvTellFriends.adapter?.notifyDataSetChanged()
        }
    }


    private fun initUI() {
        onClick()
        tellFriendsRV()
    }

    private fun tellFriendsRV() {
        binding.rvTellFriends.adapter = AthleteTellFriendsAdapter(this,contactList)
    }

    private fun onClick() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}