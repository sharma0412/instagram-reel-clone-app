package com.instagramreel.ui.ui.scout.bottombarfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.instagramreel.databinding.FragmentInboxBinding
import com.instagramreel.ui.ui.scout.adapter.InboxAdapter


class InboxFragment : Fragment() {

    lateinit var binding: FragmentInboxBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInboxBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        onClick()
        inboxRV()
        initSwipe()
    }

    private fun initSwipe() {

        /*val touchListener =
            RecyclerTouchListener(requireActivity(), binding?.rvInbox)
        touchListener
            .setClickable(object :
                RecyclerTouchListener.OnRowClickListener {
                override fun onRowClicked(position: Int) {
                    Log.d("massage", String())
                }

                override fun onIndependentViewClicked(
                    independentViewID: Int,
                    position: Int
                ) {
                }
            })

            .setSwipeOptionViews(R.id.layoutBG)

            .setSwipeable(R.id.layoutFG, R.id.layoutBG) { viewID, position ->

                job_applied_id = dataList[position].job_applied_id

                when (viewID) {
                    R.id.Reject -> {
//                                       updateJobApplicantUser(job_applied_id.toString(),"1", "")
                        val action =
                            AllApplicantsAgencyFragmentDirections.actionAllApplicantsAgencyFragmentToSelectRejectReasonFragment(
                                args.jobShortList, response.body[position]
                            )
                        findNavController().navigate(action)
                    }
                    R.id.ShortListed -> {
                        updateJobApplicantUser(job_applied_id.toString(), "2","")
                    }
                    R.id.InterView -> {
                        updateJobApplicantUser(job_applied_id.toString(), "3","")
                    }
                }
            }

        binding?.rvInbox?.addOnItemTouchListener(touchListener)*/
    }

    private fun inboxRV() {
        binding.rvInbox.adapter = InboxAdapter(this)
    }

    private fun onClick() {
        binding.apply {

        }
    }
}