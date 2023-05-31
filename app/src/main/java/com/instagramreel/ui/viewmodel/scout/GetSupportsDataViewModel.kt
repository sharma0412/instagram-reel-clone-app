package com.instagramreel.ui.viewmodel.scout

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagramreel.ui.model.scout.getsupportsdata.GetSportsResponse
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.utils.Resource
import com.instagramreel.ui.utils.getError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GetSupportsDataViewModel (var application: Application, var repository: Repository): ViewModel() {
    var getData = MutableLiveData<Resource<GetSportsResponse>>()

    fun getSportsList() {
        getData.value = Resource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getSportsData()
                withContext(Dispatchers.Main)
                {
                    if (response.code() == 200 && response.isSuccessful) {
                        if (response.body()?.success!!) {
                            if (response.body() != null) {
                                getData.value = Resource.success(
                                    data = response.body()!!,
                                    message = response.body()?.message.toString()
                                )
                            }
                        } else if (response.body()?.code == 204) {
                            getData.value = Resource.error(
                                data = null,
                                message = response.body()?.message.toString()
                            )
                        } else if (response.body()?.code == 405) {
                            //  showErrorDialog2(context,response.body()?.message.toString() )
                        } else {
                            getData.value = Resource.error(
                                data = null,
                                message = response.body()?.message.toString()
                            )
                        }
                    } else if (response.code() == 204) {
                        getData.value = Resource.error(
                            data = null,
                            message = response.body()?.message.toString()
                        )
                    } else {
                        getData.value = Resource.error(data = null, message = response.message())
                    }
                }
            } catch (throwable: Throwable) {
                withContext(Dispatchers.Main)
                {
                    getData.value = Resource.error(data = null, message = getError(throwable))
                }
            }
        }
    }
}