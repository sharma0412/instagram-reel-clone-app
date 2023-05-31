package com.instagramreel.ui.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.viewmodel.auth.AuthenticationViewModel
import com.instagramreel.ui.viewmodel.changepassword.ChangePasswordViewModel
import java.lang.IllegalArgumentException

class AuthenticationViewModelFactory (private  val application: Application, private  val repository: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> {
                AuthenticationViewModel(application, repository) as T
            }
            modelClass.isAssignableFrom(ChangePasswordViewModel::class.java) -> {
                ChangePasswordViewModel(application, repository) as T
            }
            else -> throw  IllegalArgumentException("Class not found")
        }
    }
}