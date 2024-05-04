package com.example.flickrbrowser2

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class GetFlickrJsonData(private val listener: OnDataAvailable) : AsyncTask<String,Void,ArrayList<Photo>>() {//here OnDataAvailable is same as OnDownloadComplete. it is also a callback listener
    private val TAG = "GetFlickrJsonData"
    interface OnDataAvailable{
        fun onDataAvailable(data: List<Photo>)
        fun onError(exception: Exception)//this function will be called if there is an error while parsing the data
    }


    override fun doInBackground(vararg params: String?): ArrayList<Photo> {
        Log.d(TAG,"doInBackground starts")

        val photoList = ArrayList<Photo>()
        try {
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("items")//it will search for a array named as "items" in the data we downloaded
            for (i in 0 until itemsArray.length()){
                val jsonPhoto = itemsArray.getJSONObject(i)
                val title = jsonPhoto.getString("title")
                val author = jsonPhoto.getString("author")
                val authorID = jsonPhoto.getString("author_id")
                val tags = jsonPhoto.getString("tags")

                val jsonMedia = jsonPhoto.getJSONObject("media")//here media is a object which contains m inside it. so we are retrieving media first
                val photoUrl = jsonMedia.getString("m")//this m is inside media
                val link = photoUrl.replaceFirst("_m.jpg","_b.jpg")//when the item in the list(which is in photo class) is tapped, we will launch another activity to display the photograph much larger
                //so m will give the url for small image and it will be replaced with b which gives the url for the large image

                val photoObject = Photo(title,author,authorID,link,tags,photoUrl)

                photoList.add(photoObject)
                Log.d(TAG,"doInBackground $photoObject")
            }
        }catch (e: JSONException){
            e.printStackTrace()
            Log.e(TAG,"doInBackground: Error processing Json data. ${e.message}")
            cancel(true)
            listener.onError(e)//to let the caller know if any error has occured
        }
        Log.d(TAG,"doInBackground ends")
        return photoList
    }
    override fun onPostExecute(result: ArrayList<Photo>) {
        Log.d(TAG,"onPostExecute starts")
        super.onPostExecute(result)
        listener.onDataAvailable(result)
        Log.d(TAG,"onPostExecute ends")
    }
}