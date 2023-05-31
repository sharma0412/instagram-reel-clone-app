package com.instagramreel.ui.viewmodel.scout

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagramreel.ui.model.athlete.updatedetails.AthletUpdateRequest
import com.instagramreel.ui.model.athlete.updatedetails.AthleteUpdateDetailsResponse
import com.instagramreel.ui.model.scout.updatedetails.UpdateDetailsRequest
import com.instagramreel.ui.model.scout.updatedetails.UpdateDetailsResponse
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.utils.Resource
import com.instagramreel.ui.utils.getError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateDetailsViewModel(var application: Application, var repository: Repository) : ViewModel() {
    var details = MutableLiveData<Resource<UpdateDetailsResponse>>()
    var atheletedetails = MutableLiveData<Resource<AthleteUpdateDetailsResponse>>()

    fun onUpdateDetails(model: UpdateDetailsRequest) {
        details.value = Resource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onUpdateScoutDetails(model)
                withContext(Dispatchers.Main)
                {
                    if (response.code() == 200 && response.isSuccessful) {
                        if (response.body()?.success!!) {
                            if (response.body() != null) {
                                details.value = Resource.success(
                                    data = response.body()!!,
                                    message = response.body()?.message.toString()
                                )
                            }
                        } else if (response.body()?.code == 204) {
                            details.value = Resource.error(
                                data = null,
                                message = response.body()?.message.toString()
                            )
                        } else if (response.body()?.code == 405) {
                            //  showErrorDialog2(context,response.body()?.message.toString() )
                        } else {
                            details.value = Resource.error(
                                data = null,
                                message = response.body()?.message.toString()
                            )
                        }
                    } else if (response.code() == 204) {
                        details.value = Resource.error(
                            data = null,
                            message = response.body()?.message.toString()
                        )
                    } else {
                        details.value = Resource.error(data = null, message = response.message())
                    }
                }
            } catch (throwable: Throwable) {
                withContext(Dispatchers.Main)
                {
                    details.value = Resource.error(data = null, message = getError(throwable))
                }
            }
        }
    }

    fun onAtheUpdateDetails(model: AthletUpdateRequest) {
        atheletedetails.value = Resource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.onUpdateAthleteDetails(model)
                withContext(Dispatchers.Main)
                {
                    if (response.code() == 200 && response.isSuccessful) {
                        if (response.body()?.success!!) {
                            if (response.body() != null) {
                                atheletedetails.value = Resource.success(
                                    data = response.body()!!,
                                    message = response.body()?.message.toString()
                                )
                            }
                        } else if (response.body()?.code == 204) {
                            atheletedetails.value = Resource.error(
                                data = null,
                                message = response.body()?.message.toString()
                            )
                        } else if (response.body()?.code == 405) {
                            //  showErrorDialog2(context,response.body()?.message.toString() )
                        } else {
                            atheletedetails.value = Resource.error(
                                data = null,
                                message = response.body()?.message.toString()
                            )
                        }
                    } else if (response.code() == 204) {
                        atheletedetails.value = Resource.error(
                            data = null,
                            message = response.body()?.message.toString()
                        )
                    } else {
                        atheletedetails.value =
                            Resource.error(data = null, message = response.message())
                    }
                }
            } catch (throwable: Throwable) {
                withContext(Dispatchers.Main)
                {
                    atheletedetails.value =
                        Resource.error(data = null, message = getError(throwable))
                }
            }
        }
    }
}