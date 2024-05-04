package com.example.flickrbrowser2

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.preference.PreferenceManager

class SearchActivity : BaseActivity() {
    private val TAG = "SearchActivity"
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, ".onCreate: starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        activateToolbar(true)
        Log.d(TAG, ".onCreate: ends")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, ".onCreateOptionsMenu: starts")
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager//to get a searchManager object
        searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView//we are giving the reference to searchView object
        val searchableInfo = searchManager.getSearchableInfo(componentName)//to get the searchable info from searchable.xml searched by user
        searchView?.setSearchableInfo(searchableInfo)//searchable info is set into searchView widget to configure it
//        Log.d(TAG,".onCreateOptionsMenu: $componentName")
//        Log.d(TAG,".onCreateOptionsMenu: hint is ${searchView?.queryHint}")
//        Log.d(TAG,".onCreateOptionsMenu: $searchableInfo")

        searchView?.isIconified = false//when clicked on search icon this will help to directly get the keyboard  //change it to true and see to know what happens

        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, ".onQueryTextSubmit: called")

                val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)//to get a shared preferences object by calling preferences. we are using "applicationContext" because searchActivity will store the data and MainActivity will retrieve the data. As two contexts are there we use applicationContext.
                sharedPref.edit().putString(FLICKR_QUERY,query).apply()//we are using FLICKR_QUERY constant that we defined in the baseActivity as a key
                searchView?.clearFocus()//while using an external keyboard

                finish()//this will return to the activity which launched it.Here its mainActivity,So, it will return to mainActivity
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchView?.setOnCloseListener {//this will close the searchActivity when clicked on "cancel" which appears after clicking on "search icon"
            finish()
            false
        }

        Log.d(TAG, ".onCreateOptionsMenu: returning")
        return true
    }
}









//class SearchActivity : AppCompatActivity() {
//
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivitySearchBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivitySearchBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setSupportActionBar(binding.toolbar)
//
//        val navController = findNavController(R.id.nav_host_fragment_content_search)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_search)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
//}