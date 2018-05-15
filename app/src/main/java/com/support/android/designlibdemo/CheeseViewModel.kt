package com.support.android.designlibdemo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

class CheeseViewModel : ViewModel() {
    val cheeses: LiveData<Resource<List<Cheese>>> = CheeseLiveData()
}