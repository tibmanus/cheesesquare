package com.support.android.designlibdemo

import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log
import java.io.IOException

class CheeseLiveData : LiveData<List<Cheese>>() {

    @Override
    override fun onActive() {
        super.onActive()
        updateValue()
    }

    private fun updateValue() {
        var count = 0
        val maxTries = 5
        if (value == null) {
            try {
                value = CheeseApi.listCheeses(30)
            } catch (e: IOException) {
                Log.d("CheeseLiveData", "Api error, retry...")
                if (++count >= maxTries) throw e
            }
        }
    }

}