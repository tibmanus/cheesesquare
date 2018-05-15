package com.support.android.designlibdemo

import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log
import java.io.IOException

class CheeseLiveData : LiveData<List<Cheese>>() {

    init {
        CheeseTask().execute()
    }

    inner class CheeseTask : AsyncTask<Void, Void, List<Cheese>>() {
        override fun doInBackground(vararg p0: Void?): List<Cheese>? {
            var count = 0
            val maxTries = 5
            try {
                return CheeseApi.listCheeses(30)
            } catch (e: IOException) {
                Log.d("CheeseLiveData", "Api error, retry...")
                if (++count >= maxTries) {
                    Log.d("CheeseLiveData", e.toString())
                }
                return null
            }
        }

        override fun onPostExecute(result: List<Cheese>?) {
            super.onPostExecute(result)
            value = result
        }

    }

}