package com.instagramreel.ui.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.viewmodel.athlete.kickoff.KickoffViewModel
import com.instagramreel.ui.viewmodel.profile.ProfileDetailsViewModel
import com.instagramreel.ui.viewmodel.bucketviewmodel.BucketDetailsViewModel
import com.instagramreel.ui.viewmodel.reels.GetReelsViewModel
import com.instagramreel.ui.viewmodel.reels.UploadReelsViewModel
import com.instagramreel.ui.viewmodel.scout.GetMyDetailsViewModel
import com.instagramreel.ui.viewmodel.scout.GetSupportsDataViewModel
import com.instagramreel.ui.viewmodel.scout.UpdateDetailsViewModel

class ViewModelFactory (private  val application: Application, private  val repository: Repository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GetSupportsDataViewModel::class.java) -> {
                GetSupportsDataViewModel(application, repository) as T
            }
            modelClass.isAssignableFrom(UpdateDetailsViewModel::class.java) -> {
                UpdateDetailsViewModel(application, repository) as T
            }
            modelClass.isAssignableFrom(GetMyDetailsViewModel::class.java) -> {
                GetMyDetailsViewModel(application, repository) as T
            }
            modelClass.isAssignableFrom(GetReelsViewModel::class.java) -> {
                GetReelsViewModel(application, repository) as T
            }
            modelClass.isAssignableFrom(KickoffViewModel::class.java) -> {
                KickoffViewModel(application, repository) as T
            }
            modelClass.isAssignableFrom(BucketDetailsViewModel::class.java) -> {
                BucketDetailsViewModel(application, repository) as T
            }
            modelClass.isAssignableFrom(UploadReelsViewModel::class.java) -> {
                UploadReelsViewModel(application, repository) as T
            }
            modelClass.isAssignableFrom(ProfileDetailsViewModel::class.java) -> {
                ProfileDetailsViewModel(application, repository) as T
            }
            else -> throw  IllegalArgumentException("Class not found")
        }
    }
}