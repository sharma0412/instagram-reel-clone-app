package com.instagramreel.ui.network

import com.instagramreel.ui.model.athlete.getmydetails.GetDetailsRequest
import com.instagramreel.ui.model.athlete.getmydetails.ProfileDetailsResponse
import com.instagramreel.ui.model.athlete.kickoff.*
import com.instagramreel.ui.model.athlete.updatedetails.AthletUpdateRequest
import com.instagramreel.ui.model.athlete.updatedetails.AthleteUpdateDetailsResponse
import com.instagramreel.ui.model.auth.otpverification.NumberRequest
import com.instagramreel.ui.model.auth.otpverification.NumberResponse
import com.instagramreel.ui.model.auth.otpverification.OtpRequest
import com.instagramreel.ui.model.auth.otpverification.OtpResponse
import com.instagramreel.ui.model.auth.signinmodel.SignInRequest
import com.instagramreel.ui.model.auth.signinmodel.SignInResponse
import com.instagramreel.ui.model.auth.signupmodel.SignUpRequest
import com.instagramreel.ui.model.auth.signupmodel.SignUpResponse
import com.instagramreel.ui.model.bucket.BucketDetailsResponse
import com.instagramreel.ui.model.changepassword.ChangePasswordRequest
import com.instagramreel.ui.model.changepassword.ChangePasswordResponse
import com.instagramreel.ui.model.scout.getsupportsdata.GetSportsResponse
import com.instagramreel.ui.model.scout.updatedetails.UpdateDetailsRequest
import com.instagramreel.ui.model.scout.updatedetails.UpdateDetailsResponse
import com.instagramreel.ui.model.getmydetail.GetMyDetailsResponse
import com.instagramreel.ui.model.reels.*
import com.instagramreel.ui.model.scout.getmydetails.ScoutDetailsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @POST("auth/signUp")
    suspend fun onSignUp(@Body body: SignUpRequest): Response<SignUpResponse>

    @POST("auth/login")
    suspend fun onLogIn(@Body body: SignInRequest): Response<SignInResponse>

    @POST("auth/mobileVerification")
    suspend fun onNumberVerification(@Body body: NumberRequest): Response<NumberResponse>

    @POST("auth/checkOtp")
    suspend fun onOtpVerification(@Body body: OtpRequest): Response<OtpResponse>

    @POST("auth/changePassword")
    suspend fun onChangePassword(@Body body: ChangePasswordRequest): Response<ChangePasswordResponse>

    @POST("reels/reelLikeDislike")
    suspend fun onLike(@Body body: LikeOrDislikeRequest): Response<LikeOrDislikeResponse>

    @GET("auth/getSportList")
    suspend fun getSupports(): Response<GetSportsResponse>

    @GET("auth/getMyDetail")
    suspend fun getMyDetails(): Response<GetMyDetailsResponse>

    @GET("reels/reelsList")
    suspend fun getAllReels(): Response<ReelsResponse>

    @POST("auth/updateScoutDetail")
    suspend fun onUpdateScoutDetails(@Body body: UpdateDetailsRequest): Response<UpdateDetailsResponse>

    @POST("auth/updatetAthleteDetail")
    suspend fun onUpdateAthleteDetails(@Body body: AthletUpdateRequest): Response<AthleteUpdateDetailsResponse>

    @GET("reels/KickoffList")
    suspend fun getKickoffList(): Response<KickOffResponse>

    @GET("auth/S3Detail")
    suspend fun getBucketDetails(): Response<BucketDetailsResponse>

    @POST("auth/AthletMyDetail")
    suspend fun getAthleteProfileData(@Body body: GetDetailsRequest): Response<ProfileDetailsResponse>

    @POST("auth/scoutMyDetail")
    suspend fun getScoutProfileData(@Body body: GetDetailsRequest): Response<ScoutDetailsResponse>

    @POST("reels/reelsupload")
    suspend fun uploadReels(@Body body: UploadReelsRequest): Response<UploadReelsResponse>

    @POST("reels/joinKickoff")
    suspend fun joinKickoff(@Body body: KickoffJoinRequest): Response<KickoffJoinResponse>

    @POST("reels/KickoffDetail")
    suspend fun kickoffDetails(@Body body: KickoffDetailsRequest): Response<KickoffDetailsResponse>
}