package com.instagramreel.ui.model.athlete.updatedetails

data class AthleteUpdateDetailsResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
) {
    data class Body(
        val bio: String,
        val country_code: String,
        val country_name: Any,
        val created_at: String,
        val deviceToken: Any,
        val deviceType: Any,
        val dob: String,
        val email: String,
        val fullname: String,
        val height: String,
        val id: Int,
        val is_verified: Int,
        val otp: String,
        val password: String,
        val phone_code: Int,
        val phone_no: Long,
        val position: String,
        val profile_pic: Any,
        val role: Int,
        val updated_at: String,
        val weight: String
    )
}