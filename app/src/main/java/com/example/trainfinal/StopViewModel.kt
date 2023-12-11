package com.example.trainfinal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StopViewModel: ViewModel() {
    private val viewStops = MutableLiveData<ArrayList<RouteStops>>()

    fun getData(): ArrayList<RouteStops>? {
        return viewStops.value
    }

    fun updateData(stops: ArrayList<RouteStops>) {
        viewStops.postValue(stops)
    }
}