package com.instagramreel.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
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
import com.instagramreel.ui.ui.athlete.adapter.PredictionAdapter

class LocationPickerDialog(context: Context,val listener: Listener,val view: View) : Dialog(context),
    PredictionAdapter.PredictionCallbacks {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var binding: FragmentPlaceSelectionBinding? = null
    private var placesClient: PlacesClient? = null
    private var predictionAdapter: PredictionAdapter? = null
    private var suggestionsList = ArrayList<AutocompletePrediction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_place_selection, null, false)
        window?.decorView?.left = 50
        window?.decorView?.right = 50
        window?.attributes?.gravity = Gravity.CENTER
        setContentView(binding?.root!!)
        setCancelable(true)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        Places.initialize(context, context.getString(R.string.map_key))

        placesClient = Places.createClient(context)

        binding?.etSearchCountry?.doOnTextChanged { text, _, _, _ ->
            text?.let {
                setPlacesRequest(it.toString())
            }
        }

        binding?.ivBack?.setOnClickListener{
            dismiss()
        }
    }

    private fun setPlacesRequest(s: String) {

        suggestionsList = ArrayList()

        val token = AutocompleteSessionToken.newInstance()

        predictionAdapter = PredictionAdapter(
            context,
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
                suggestionsList.addAll(response.autocompletePredictions.distinctBy {
                    it.getFullText(
                        null
                    ).toString()
                })
                if (suggestionsList.size > 0) predictionAdapter!!.updateData(suggestionsList)
            }
        }
    }

    interface Listener {
        fun onSelectLocation(location: String)
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

                context.hideKeyboard(view)

                listener.onSelectLocation(data.getFullText(null).toString())
                dismiss()

            } else {
                Toast.makeText(context, "Unable to fetch Location", Toast.LENGTH_SHORT)
                    .show()
            }
        }.addOnFailureListener {
            Toast.makeText(
                context,
                "Unable to fetch location",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun Context.hideKeyboard(view: View) {
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}