package com.example.trainfinal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StopViewModel: ViewModel() {
    private val viewStops = MutableLiveData<List<RouteStops>>()

    fun getData(): List<RouteStops>? {
        return viewStops.value
    }

    fun updateData(stops: List<RouteStops>) {
        viewStops.postValue(stops)
    }
}