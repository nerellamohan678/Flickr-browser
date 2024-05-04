package com.example.flickrbrowser2

import android.os.AsyncTask
import android.util.Log
import androidx.loader.content.AsyncTaskLoader
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus{
    OK, IDLE,NOT_INITIALISED, FAILED_ON_EMPTY, PERMISSIONS_ERROR, ERROR
}
class GetRawData(private val listener: OnDownloadComplete) : AsyncTask<String, Void, String>(){
    private val TAG = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE

    interface OnDownloadComplete{//we are using this interface because this onDownloadComplete might not be present in mainActivity and to avoid that problem we used this
        fun onDownloadComplete(data: String, status: DownloadStatus)
    }
//    private var listener: MainActivity? = null //this is a callBack listener which tek=lls the mainactivity

//    fun setDownloadCompleteListner(callbackIObject: MainActivity){//to tell mainActivity when the download completes
//        listener = callbackIObject
//    }
    override fun onPostExecute(result: String) {
        Log.d(TAG,"onPostExecute called")
        listener.onDownloadComplete(result,downloadStatus)
    }

    override fun doInBackground(vararg params: String?): String {
        if (params[0] ==null){
            downloadStatus = DownloadStatus.NOT_INITIALISED
            return "NO URL specified"
        }

        try {
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        }catch (e: Exception){
            val errorMessage = when (e){
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    "doInBAckground: Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_ON_EMPTY
                    "doInBAckground: IO Exception reading data:  ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    "doInBAckground: SecurityException: Need permission ${e.message}"
                }else ->{
                    downloadStatus = DownloadStatus.ERROR
                    "Unknown error: ${e.message}"
                }
            }
            Log.e(TAG,errorMessage)

            return errorMessage
        }
    }
}