package com.instagramreel.ui.viewmodel.athlete.kickoff

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagramreel.ui.model.athlete.kickoff.*
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.utils.Resource
import com.instagramreel.ui.utils.getError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KickoffViewModel (var application: Application, var repository: Repository): ViewModel() {
    var kickOff = MutableLiveData<Resource<KickOffResponse>>()
    var kickOffJoin = MutableLiveData<Resource<KickoffJoinResponse>>()
    var kickOffDetails = MutableLiveData<Resource<KickoffDetailsResponse>>()

    fun getKickoffList() {
        kickOff.value = Resource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getKickoffList()
                withContext(Dispatchers.Main)
                {
                    if (response.code() == 200 && response.isSuccessful) {
                        if (response.body()?.success!!) {
                            if (response.body() != null) {
                                kickOff.value = Resource.success(
                                    data = response.body()!!,
                                    message = response.body()?.message.toString()
                                )
                            }
                        } else if (response.body()?.code == 204) {
                            kickOff.value = Resource.error(
                                data = null,
                                message = response.body()?.message.toString()
                            )
                        } else if (response.body()?.code == 405) {
                            //  showErrorDialog2(context,response.body()?.message.toString() )
                        } else {
                            kickOff.value = Resource.error(
                                data = null,
                                message = response.body()?.message.toString()
                            )
                        }
                    } else if (response.code() == 204) {
                        kickOff.value = Resource.error(
                            data = null,
                            message = response.body()?.message.toString()
                        )
                    } else {
                        kickOff.value = Resource.error(data = null, message = response.message())
                    }
                }
            } catch (throwable: Throwable) {
                withContext(Dispatchers.Main)
                {
                    kickOff.value = Resource.error(data = null, message = getError(throwable))
                }
            }
        }
    }

    fun joinKickoff(model: KickoffJoinRequest) {
        kickOffJoin.value = Resource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.joinKickoff(model)
                withContext(Dispatchers.Main)
                {
                    if (response.code() == 200 && response.isSuccessful) {
                        if (response.body()?.success!!) {
                            if (response.body() != null) {
                                kickOffJoin.value = Resource.success(
                                    data = response.body()!!,
                                    message = response.body()?.message.toString()
                                )
                            }
                        } else if (response.body()?.code == 204) {
                            kickOffJoin.value = Resource.error(
                                data = null,
                                message = response.body()?.message.toString()
                            )
                        } else if (response.body()?.code == 405) {
                            //  showErrorDialog2(context,response.body()?.message.toString() )
                        } else {
                            kickOffJoin.value = Resource.error(
                                data = null,
                                message = response.body()?.message.toString()
                            )
                        }
                    } else if (response.code() == 204) {
                        kickOffJoin.value = Resource.error(
                            data = null,
                            message = response.body()?.message.toString()
                        )
                    } else {
                        kickOffJoin.value = Resource.error(data = null, message = response.message())
                    }
                }
            } catch (throwable: Throwable) {
                withContext(Dispatchers.Main)
                {
                    kickOffJoin.value = Resource.error(data = null, message = getError(throwable))
                }
            }
        }
    }

    fun kickoffDetails(model: KickoffDetailsRequest) {
        kickOffDetails.value = Resource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.kickoffDetails(model)
                withContext(Dispatchers.Main)
                {
                    if (response.code() == 200 && response.isSuccessful) {
                        if (response.body()?.success!!) {
                            if (response.body() != null) {
                                kickOffDetails.value = Resource.success(
                                    data = response.body()!!,
                                    message = response.body()?.message.toString()
                                )
                            }
                        } else if (response.body()?.code == 204) {
                            kickOffDetails.value = Resource.error(
                                data = null,
                                message = response.body()?.message.toString()
                            )
                        } else if (response.body()?.code == 405) {
                            //  showErrorDialog2(context,response.body()?.message.toString() )
                        } else {
                            kickOffDetails.value = Resource.error(
                                data = null,
                                message = response.body()?.message.toString()
                            )
                        }
                    } else if (response.code() == 204) {
                        kickOffDetails.value = Resource.error(
                            data = null,
                            message = response.body()?.message.toString()
                        )
                    } else {
                        kickOffDetails.value = Resource.error(data = null, message = response.message())
                    }
                }
            } catch (throwable: Throwable) {
                withContext(Dispatchers.Main)
                {
                    kickOffDetails.value = Resource.error(data = null, message = getError(throwable))
                }
            }
        }
    }
}