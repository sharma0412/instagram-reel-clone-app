package com.instagramreel.ui.repository

import com.instagramreel.ui.model.athlete.getmydetails.GetDetailsRequest
import com.instagramreel.ui.model.athlete.getmydetails.ProfileDetailsResponse
import com.instagramreel.ui.model.athlete.kickoff.*
import com.instagramreel.ui.model.athlete.updatedetails.AthletUpdateRequest
import com.instagramreel.ui.model.athlete.updatedetails.AthleteUpdateDetailsResponse
import com.instagramreel.ui.model.changepassword.ChangePasswordRequest
import com.instagramreel.ui.model.changepassword.ChangePasswordResponse
import com.instagramreel.ui.model.scout.getsupportsdata.GetSportsResponse
import com.instagramreel.ui.model.auth.otpverification.NumberRequest
import com.instagramreel.ui.model.auth.otpverification.NumberResponse
import com.instagramreel.ui.model.auth.otpverification.OtpRequest
import com.instagramreel.ui.model.auth.otpverification.OtpResponse
import com.instagramreel.ui.model.auth.signinmodel.SignInRequest
import com.instagramreel.ui.model.auth.signinmodel.SignInResponse
import com.instagramreel.ui.model.auth.signupmodel.SignUpRequest
import com.instagramreel.ui.model.auth.signupmodel.SignUpResponse
import com.instagramreel.ui.model.bucket.BucketDetailsResponse
import com.instagramreel.ui.model.scout.updatedetails.UpdateDetailsRequest
import com.instagramreel.ui.model.scout.updatedetails.UpdateDetailsResponse
import com.instagramreel.ui.network.RetrofitClient
import com.instagramreel.ui.model.getmydetail.GetMyDetailsResponse
import com.instagramreel.ui.model.reels.*
import com.instagramreel.ui.model.scout.getmydetails.ScoutDetailsResponse
import retrofit2.Response


class Repository {

    suspend fun signup(body: SignUpRequest): Response<SignUpResponse> {
        return  RetrofitClient().getapi().onSignUp(body)
    }

    suspend fun logIn(body: SignInRequest): Response<SignInResponse> {
        return  RetrofitClient().getapi().onLogIn(body)
    }

    suspend fun number(body: NumberRequest): Response<NumberResponse> {
        return  RetrofitClient().getapi().onNumberVerification(body)
    }

    suspend fun otp(body: OtpRequest): Response<OtpResponse> {
        return  RetrofitClient().getapi().onOtpVerification(body)
    }

    suspend fun changePassword(body: ChangePasswordRequest): Response<ChangePasswordResponse> {
        return  RetrofitClient().getapi().onChangePassword(body)
    }

    suspend fun onLike(body: LikeOrDislikeRequest): Response<LikeOrDislikeResponse> {
        return  RetrofitClient().getapi().onLike(body)
    }

    suspend fun getSportsData(): Response<GetSportsResponse> {
        return  RetrofitClient().getapi().getSupports()
    }

    suspend fun getMyDetails(): Response<GetMyDetailsResponse> {
        return  RetrofitClient().getapi().getMyDetails()
    }

    suspend fun getAllReels(): Response<ReelsResponse> {
        return  RetrofitClient().getapi().getAllReels()
    }

    suspend fun onUpdateScoutDetails(body: UpdateDetailsRequest): Response<UpdateDetailsResponse> {
        return  RetrofitClient().getapi().onUpdateScoutDetails(body)
    }

    suspend fun onUpdateAthleteDetails(body: AthletUpdateRequest): Response<AthleteUpdateDetailsResponse> {
        return  RetrofitClient().getapi().onUpdateAthleteDetails(body)
    }

    suspend fun getKickoffList(): Response<KickOffResponse> {
        return  RetrofitClient().getapi().getKickoffList()
    }

    suspend fun getBucketDetails(): Response<BucketDetailsResponse> {
        return  RetrofitClient().getapi().getBucketDetails()
    }

    suspend fun getAthleteProfileData(body: GetDetailsRequest): Response<ProfileDetailsResponse> {
        return  RetrofitClient().getapi().getAthleteProfileData(body)
    }

    suspend fun getScoutProfileData(body: GetDetailsRequest): Response<ScoutDetailsResponse> {
        return  RetrofitClient().getapi().getScoutProfileData(body)
    }

    suspend fun onUploadReels(body: UploadReelsRequest): Response<UploadReelsResponse> {
        return  RetrofitClient().getapi().uploadReels(body)
    }

    suspend fun joinKickoff(body: KickoffJoinRequest): Response<KickoffJoinResponse> {
        return  RetrofitClient().getapi().joinKickoff(body)
    }

    suspend fun kickoffDetails(body: KickoffDetailsRequest): Response<KickoffDetailsResponse> {
        return  RetrofitClient().getapi().kickoffDetails(body)
    }
}