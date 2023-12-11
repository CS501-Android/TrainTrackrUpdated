package com.example.trainfinal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trainfinal.TrainRouteResponse
import kotlinx.coroutines.launch

class TrainRouteViewModel : ViewModel() {
    private val _trainRoutes = MutableLiveData<TrainRouteResponse>()
    private val trainRoutes: LiveData<TrainRouteResponse> = _trainRoutes
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchTrainRoutes(fromPlace: String, toPlace: String, time: String, date: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.service.getTrainRoutes(fromPlace, toPlace, time, date)
                if (response.isSuccessful) {
                    _trainRoutes.postValue(response.body())
                } else {
                    _error.postValue("API Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Network Error: ${e.message}")
            }
        }
    }

    fun getRoutes(): TrainRouteResponse? {
        return trainRoutes.value
    }

    fun getError(): String? {
        return _error.value
    }

}
