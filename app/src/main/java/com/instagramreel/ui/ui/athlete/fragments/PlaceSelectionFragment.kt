package com.instagramreel.ui.ui.athlete.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.instagramreel.R
import com.instagramreel.databinding.FragmentPlaceSelectionBinding
import com.instagramreel.ui.sharedPref.AppPrefs
import com.instagramreel.ui.ui.athlete.adapter.PredictionAdapter
import com.instagramreel.ui.utils.hideKeyboard
import java.util.*


class PlaceSelectionFragment : Fragment(), PredictionAdapter.PredictionCallbacks,
    View.OnClickListener {

    private val TAG = "PlaceSelectionFragment"
    private var binding: FragmentPlaceSelectionBinding? = null
    private var suggestionsList = ArrayList<AutocompletePrediction>()
    private var predictionAdapter: PredictionAdapter? = null
    private var placesClient: PlacesClient? = null
    private val appPrefs = AppPrefs(requireContext())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentPlaceSelectionBinding.inflate(layoutInflater, container, false)
            initViews()
        }
        return binding?.root
    }

    override fun onClick(v: View?) {
        when (v) {
            binding?.ivBack -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun initViews() {

        binding?.onclick = this
        Places.initialize(requireContext(), getString(R.string.map_key))
        placesClient = Places.createClient(requireContext())

        binding?.etSearchCountry?.doOnTextChanged { text, _, _, _ ->
            text?.let {
                setPlacesRequest(it.toString())
            }
        }

    }

    private fun setPlacesRequest(s: String) {

        suggestionsList = ArrayList()

        val token = AutocompleteSessionToken.newInstance()

        predictionAdapter = PredictionAdapter(
            requireContext(),
            suggestionsList, this
        )

        binding?.rvSuggestions?.adapter = predictionAdapter

        if (s.isEmpty()) {
            binding?.rvSuggestions?.visibility = View.GONE
            return
        } else {
            binding?.rvSuggestions?.visibility = View.VISIBLE
        }

        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setTypeFilter(TypeFilter.CITIES)
            .setQuery(s)
            .build()

        placesClient!!.findAutocompletePredictions(request).addOnSuccessListener { response ->
            if (predictionAdapter != null) {
                suggestionsList.clear()
                suggestionsList.addAll(response.autocompletePredictions.distinctBy { it.getFullText(null).toString() })
                if (suggestionsList.size > 0) predictionAdapter!!.updateData(suggestionsList)
            }
        }.addOnCompleteListener {
            Log.d(TAG, "onTextChanged: Search Completed")
        }.addOnFailureListener {
            Log.d(TAG, "onTextChanged: $it")
        }

    }

    override fun onClickAddressItem(data: AutocompletePrediction) {

        val placeFields = listOf(
            Place.Field.ID, Place.Field.NAME,
            Place.Field.LAT_LNG
        )

        // Construct a request object, passing the place ID and fields array.
        val request = FetchPlaceRequest.newInstance(data.placeId, placeFields)
        placesClient!!.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            if (place.latLng != null) {
                Log.d(TAG, "onClickAddressItem: Lat ${place.latLng!!.latitude}")
                Log.d(TAG, "onClickAddressItem: long ${place.latLng!!.longitude}")

                requireContext().hideKeyboard(requireView())

                appPrefs.setString("locationName",data.getFullText(null).toString())

                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Unable to fetch Location", Toast.LENGTH_SHORT)
                    .show()
            }
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Unable to fetch location",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}