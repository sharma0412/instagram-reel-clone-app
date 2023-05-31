package com.instagramreel.ui.model.athlete.getmydetails

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileDetailsResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("body")
	val body: Body? = null
):Parcelable

@Parcelize
data class Body(

	@field:SerializedName("deviceType")
	val deviceType: String? = null,

	@field:SerializedName("phone_no")
	val phoneNo: Long? = null,

	@field:SerializedName("role")
	val role: Int? = null,

	@field:SerializedName("profile_pic")
	val profilePic: String? = null,

	@field:SerializedName("bio")
	val bio: String? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("otp")
	val otp: String? = null,

	@field:SerializedName("UserReeels")
	val userReeels: ArrayList<UserReeelsItem?>? = null,

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
	val position: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("phone_code")
	val phoneCode: Int? = null,

	@field:SerializedName("height")
	val height: Int? = null,

	@field:SerializedName("Usersportdata")
	val usersportdata: ArrayList<UsersportdataItem?>? = null
):Parcelable

@Parcelize
data class UsersportdataItem(

	@field:SerializedName("sport_id")
	val sportId: String? = null,

	@field:SerializedName("sportdata")
	val sportdata: Sportdata? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
):Parcelable

@Parcelize
data class UserReeelsItem(

	@field:SerializedName("video_thumbnail")
	val videoThumbnail: String? = null,

	@field:SerializedName("sport_id")
	val sportId: String? = null,

	@field:SerializedName("video_url")
	val videoUrl: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
):Parcelable

@Parcelize
data class Sportdata(

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
):Parcelable
