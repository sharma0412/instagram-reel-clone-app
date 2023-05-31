package com.instagramreel.ui.viewmodel.auth

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagramreel.ui.model.auth.otpverification.NumberRequest
import com.instagramreel.ui.model.auth.otpverification.NumberResponse
import com.instagramreel.ui.model.auth.otpverification.OtpRequest
import com.instagramreel.ui.model.auth.otpverification.OtpResponse
import com.instagramreel.ui.model.auth.signinmodel.SignInRequest
import com.instagramreel.ui.model.auth.signinmodel.SignInResponse
import com.instagramreel.ui.model.auth.signupmodel.SignUpRequest
import com.instagramreel.ui.model.auth.signupmodel.SignUpResponse
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.utils.Resource
import com.instagramreel.ui.utils.getError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel (var application: Application, var repository: Repository): ViewModel() {
    var signUp = MutableLiveData<Resource<SignUpResponse>>()
    var login = MutableLiveData<Resource<SignInResponse>>()
    var number = MutableLiveData<Resource<NumberResponse>>()
    var otp = MutableLiveData<Resource<OtpResponse>>()

    fun onClickRegisterUser(model : SignUpRequest) {
        signUp.value = Resource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.signup(model)
                withContext(Dispatchers.Main)
                {
                    if(response.code()==200 && response.isSuccessful)
                    {
                        if(response.body()?.success!!) {
                            if(response.body()!=null) {
                                signUp.value= Resource.success(data =response.body()!!, message = response.body()?.message.toString())
                            }
                        } else if(response.body()?.code==204) {
                            signUp.value= Resource.error(data = null, message = response.body()?.message.toString())
                        }
                        else if(response.body()?.code==405) {
                          //  showErrorDialog2(context,response.body()?.message.toString() )
                        }
                        else {
                            signUp.value= Resource.error(data = null, message = response.body()?.message.toString())
                        }
                    }
                    else if(response.code()==204) {
                        signUp.value= Resource.error(data = null, message = response.body()?.message.toString())
                    }
                    else {
                        signUp.value= Resource.error(data = null, message = response.message())
                    }
                }
            } catch (throwable: Throwable) {
                withContext(Dispatchers.Main)
                {
                   signUp.value = Resource.error(data = null, message = getError(throwable))
                }
            }

        }
    }

    fun onClickLoginUser(model: SignInRequest) {
        login.value = Resource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.logIn(model)
                withContext(Dispatchers.Main)
                {
                    if(response.code()==200 && response.isSuccessful)
                    {
                            if(response.body()?.success!!) {
                                if(response.body()!=null) {
                                    login.value= Resource.success(data =response.body()!!, message = response.body()?.message.toString())
                                }
                            } else if(response.body()?.code==204) {
                            login.value= Resource.error(data = null, message = response.body()?.message.toString())
                        }
                        else if(response.body()?.code==405) {
                        }
                        else {
                            login.value= Resource.error(data = null, message = response.body()?.message.toString())
                        }
                    }
                    else if(response.code()==204) {
                        login.value= Resource.error(data = null, message = response.body()?.message.toString())
                    }
                    else {
                        login.value= Resource.error(data = null, message = response.message())
                    }
                }
            } catch (throwable: Throwable) {
                withContext(Dispatchers.Main)
                {
                    login.value = Resource.error(data = null, message = getError(throwable))
                }
            }

        }
    }

    fun onClickNumberVerification(model: NumberRequest) {
        number.value = Resource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.number(model)
                withContext(Dispatchers.Main)
                {
                    if(response.code()==200 && response.isSuccessful)
                    {
                        if(response.body()?.success!!) {
                            if(response.body()!=null) {
                                number.value= Resource.success(data =response.body()!!, message = response.body()?.message.toString())
                            }
                        } else if(response.body()?.code==204) {
                            number.value= Resource.error(data = null, message = response.body()?.message.toString())
                        }
                        else if(response.body()?.code==405) {
                            number.value= Resource.error(data = null, message = response.body()?.message.toString())
                        }
                        else {
                            number.value= Resource.error(data = null, message = response.body()?.message.toString())
                        }
                    }
                    else if(response.code()==204) {
                        number.value= Resource.error(data = null, message = response.body()?.message.toString())
                    }
                    else {
                        number.value= Resource.error(data = null, message = response.message())
                    }
                }
            } catch (throwable: Throwable) {
                withContext(Dispatchers.Main)
                {
                    number.value = Resource.error(data = null, message = getError(throwable))
                }
            }

        }
    }

    fun onOtpVerification(model: OtpRequest) {
        otp.value = Resource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.otp(model)
                withContext(Dispatchers.Main)
                {
                    if(response.code()==200 && response.isSuccessful)
                    {
                        if(response.body()?.success!!) {
                            if(response.body()!=null) {
                                otp.value= Resource.success(data =response.body()!!, message = response.body()?.message.toString())
                            }
                        } else if(response.body()?.code==204) {
                            otp.value= Resource.error(data = null, message = response.body()?.message.toString())
                        }
                        else if(response.body()?.code==405) {
                            otp.value= Resource.error(data = null, message = response.body()?.message.toString())
                        }
                        else {
                            otp.value= Resource.error(data = null, message = response.body()?.message.toString())
                        }
                    }
                    else if(response.code()==204) {
                        otp.value= Resource.error(data = null, message = response.body()?.message.toString())
                    }
                    else {
                        otp.value= Resource.error(data = null, message = response.message())
                    }
                }
            } catch (throwable: Throwable) {
                withContext(Dispatchers.Main)
                {
                    otp.value = Resource.error(data = null, message = getError(throwable))
                }
            }

        }
    }
}

