package com.instagramreel.ui.model.scout.updatedetails


import com.google.gson.annotations.SerializedName
import com.instagramreel.ui.model.ExperienceModel

data class UpdateDetailsRequest(
    @SerializedName("country_name")
    val countryName: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("experiences")
    val experiences: ArrayList<ExperienceModel>
) {
   /* data class Experience(
        @SerializedName("from")
        val from: String,
        @SerializedName("job_title")
        val jobTitle: String,
        @SerializedName("specialised_sport")
        val specialisedSport: String,
        @SerializedName("to")
        val t*/

}