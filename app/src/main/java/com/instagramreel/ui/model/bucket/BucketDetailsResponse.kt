package com.instagramreel.ui.model.bucket

data class BucketDetailsResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
) {
    data class Body(
        val Access_KEY: String,
        val Bucket_Region: String,
        val Bucket_name: String,
        val Secret_Key: String
    )
}