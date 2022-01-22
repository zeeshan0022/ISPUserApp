package com.joinhub.complaintprotaluser.utilties

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


abstract class AsyncTasks {
    private var executors: ExecutorService? =Executors.newSingleThreadExecutor()
    fun AsyncTasks() {

    }

    private fun startBackground() {
        onPreExecute()
        executors!!.execute {
            doInBackground()
            Handler(Looper.getMainLooper()).post { onPostExecute() }
        }
    }

    fun execute() {
        startBackground()
    }

    fun shutdown() {
        executors!!.shutdown()
    }

    fun isShutdown(): Boolean {
        return executors!!.isShutdown
    }

    abstract fun onPreExecute()

    abstract fun doInBackground()

    abstract fun onPostExecute()
}