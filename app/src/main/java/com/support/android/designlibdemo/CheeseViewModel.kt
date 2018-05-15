package com.support.android.designlibdemo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

class CheeseViewModel : ViewModel() {
    val cheeses: LiveData<Resource<List<Cheese>>>
        get() = _cheeses

    private val _cheeses = CheeseLiveData()

    fun refreshCheeses() {
        _cheeses.refreshValue()
    }
}