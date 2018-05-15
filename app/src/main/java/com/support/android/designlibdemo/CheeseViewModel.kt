package com.support.android.designlibdemo

import android.arch.lifecycle.ViewModel

class CheeseViewModel : ViewModel() {
    val cheeses: List<Cheese> = CheeseApi.listCheeses(30)
}