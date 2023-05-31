package com.instagramreel.ui.model.scout.getmydetails

import com.google.gson.annotations.SerializedName

data class ScoutDetailsResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("body")
	val body: Body? = null
)

data class Specialisedsport(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class UsersexpertiseItem(

	@field:SerializedName("specialised_sport")
	val specialisedSport: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("from")
	val from: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("to")
	val to: String? = null,

	@field:SerializedName("job_title")
	val jobTitle: String? = null,

	@field:SerializedName("specialisedsport")
	val specialisedsport: Specialisedsport? = null
)

data class Body(

	@field:SerializedName("deviceType")
	val deviceType: String? = null,

	@field:SerializedName("phone_no")
	val phoneNo: Long? = null,

	@field:SerializedName("Usersexpertise")
	val usersexpertise: List<UsersexpertiseItem?>? = null,

	@field:SerializedName("role")
	val role: Int? = null,

	@field:SerializedName("profile_pic")
	val profilePic: String? = null,

	@field:SerializedName("bio")
	val bio: String? = null,

	@field:SerializedName("weight")
	val weight: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("otp")
	val otp: String? = null,

	@field:SerializedName("is_verified")
	val isVerified: Int? = null,

	@field:SerializedName("deviceToken")
	val deviceToken: String? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("dob")
	val dob: String? = null,

	@field:SerializedName("country_name")
	val countryName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("position")
	val position: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("phone_code")
	val phoneCode: Int? = null,

	@field:SerializedName("height")
	val height: String? = null
)
