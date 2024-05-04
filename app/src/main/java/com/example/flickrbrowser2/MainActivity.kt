package com.example.flickrbrowser2

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.Explode
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : BaseActivity(),GetRawData.OnDownloadComplete,//we are using an interface OnDownloadComplete from getRawData
    GetFlickrJsonData.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener{
    private val TAG = "MainActivity"

    private val flickrRecyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList())
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activateToolbar(false)//we are passing false because we don't want home button to appear on mainActivity which is home page

        val recycler_view = findViewById<RecyclerView>(R.id.recycler_view)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this,recycler_view,this))
        recycler_view.adapter = flickrRecyclerViewAdapter

//        getRawData.setDownloadCompleteListner(this)//we are telling getRawData to call the onDownloadComplete function in this instance that is mainActivity

//        this below code is for floating action button
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
        Log.d(TAG, "onCreate ends")
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG,".onItemClick: starts")
        Toast.makeText(this,"Normal tap at position $position",Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG,".onItemLong: starts")
    //        Toast.makeText(this,"Long tap at position $position",Toast.LENGTH_SHORT).show()
        val photo = flickrRecyclerViewAdapter.getPhoto(position)
        if(photo!=null){
            val intent = Intent(this,PhotoDetailsActivity::class.java)//this is to change into photoDetailsActivity when clicked on it(to change thee intent) and due to those :: we can use class as a parameter
            intent.putExtra(PHOTO_TRANSFER,photo)//PHOTO_TRANSFER act's as same key which is used by both mainActivity and PhotoDetailsActivity to share the details of the photo
            startActivity(intent)
        }
    }

    private fun createUri(baseURL: String, searchCriteria: String, lang: String, matchAll: Boolean ): String{
        Log.d(TAG,"CreateUri starts")

        var uri = Uri.parse(baseURL)//it will parse create a object
        var builder = uri.buildUpon()//to get a builder
        builder = builder.appendQueryParameter("tags",searchCriteria)
        builder = builder.appendQueryParameter("tagmode",if(matchAll) "ALL" else "ANY")
        builder = builder.appendQueryParameter("lang",lang)
        builder = builder.appendQueryParameter("format","json")
        builder = builder.appendQueryParameter("nojsoncallback","1")
        uri = builder.build()

        return uri.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG, "onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG, "onOptionsItemSelected called")
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this,SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    companion object {
//        private const val TAG = "MainActivity"
//    }
    override fun onDownloadComplete(data: String, status: DownloadStatus){//here the data string will have the json code which was in the resule parameter in onPostExecute method
        if(status == DownloadStatus.OK){
            Log.d(TAG,"onDownloadComplete called")

            val getFlickerJsonData = GetFlickrJsonData(this)
            getFlickerJsonData.execute(data)
        }else{
            //download failed
            Log.d(TAG,"onDownloadComplete failed with status $status. Error message ud: $data")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {//it will receive the data of photoObject
        Log.d(TAG, "onDataAvailable Called")
        flickrRecyclerViewAdapter.loadNewData(data)
        Log.d(TAG,"onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.d(TAG,"onError called with ${exception.message}")
    }

    override fun onResume() {
        Log.d(TAG,"onResume: starts")
        super.onResume()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult = sharedPref.getString(FLICKR_QUERY,"")//here we are getting the string from search activity

        if (queryResult != null && queryResult.isNotEmpty()) {
            val url = createUri("https://api.flickr.com/services/feeds/photos_public.gne",queryResult,"en-us",true)
            val getRawData = GetRawData(this)
            getRawData.execute(url)
        }
        Log.d(TAG,"onResume: Ends")
    }
}