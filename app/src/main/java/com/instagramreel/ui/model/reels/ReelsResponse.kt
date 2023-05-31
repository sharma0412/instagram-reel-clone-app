package com.instagramreel.ui.model.reels

import com.google.gson.annotations.SerializedName

data class ReelsResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("body")
	val body: List<BodyItem?>? = null
)

data class Userdeatils(

	@field:SerializedName("deviceType")
	val deviceType: Any? = null,

	@field:SerializedName("phone_no")
	val phoneNo: Long? = null,

	@field:SerializedName("role")
	val role: Int? = null,

	@field:SerializedName("profile_pic")
	val profilePic: Any? = null,

	@field:SerializedName("bio")
	val bio: Any? = null,

	@field:SerializedName("weight")
	val weight: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("otp")
	val otp: String? = null,

	@field:SerializedName("is_verified")
	val isVerified: Int? = null,

	@field:SerializedName("deviceToken")
	val deviceToken: Any? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("Usersports")
	val usersports: List<Any?>? = null,

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
	val height: Any? = null
)

data class UsersportsItem(

	@field:SerializedName("sport_id")
	val sportId: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class BodyItem(

	@field:SerializedName("video_thumbnail")
	val videoThumbnail: String? = null,

	@field:SerializedName("reelsport")
	val reelsport: Reelsport? = null,

	@field:SerializedName("sport_id")
	val sportId: String? = null,

	@field:SerializedName("video_url")
	val videoUrl: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("isLike")
	val isLike: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("Userdeatils")
	val userdeatils: Userdeatils? = null,

	@field:SerializedName("likeCount")
	val likeCount: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("commentCount")
	val commentCount: String? = null
)

data class Reelsport(

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
