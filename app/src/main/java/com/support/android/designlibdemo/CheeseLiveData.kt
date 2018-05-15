package com.support.android.designlibdemo

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log
import java.io.IOException

class CheeseLiveData : LiveData<Resource<List<Cheese>>>() {

    init {
        CheeseTask().execute()
    }

    @SuppressLint("StaticFieldLeak")
    inner class CheeseTask : AsyncTask<Void, Void, Resource<List<Cheese>>>() {

        override fun onPreExecute() {
            super.onPreExecute()
            value = Resource.loading(null)
        }

        override fun doInBackground(vararg p0: Void?): Resource<List<Cheese>>? {
            var count = 0
            val maxTries = 5
            return try {
                Resource.success(CheeseApi.listCheeses(30))
            } catch (e: IOException) {
                Log.d("CheeseLiveData", "Api error, retry...")
                if (++count >= maxTries) {
                    Log.d("CheeseLiveData", e.toString())
                }
                Resource.error(e.toString(), null)
            }
        }

        override fun onPostExecute(result: Resource<List<Cheese>>) {
            super.onPostExecute(result)
            value = result
        }

    }

}