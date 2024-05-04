package com.example.flickrbrowser2

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.flickrbrowser2.databinding.ActivityPhotoDetailsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso

class PhotoDetailsActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)

        activateToolbar(true)

        val photo_title = findViewById<TextView>(R.id.photo_title)
        val photo_author = findViewById<TextView>(R.id.photo_author)
        val photo_image = findViewById<ImageView>(R.id.photo_image)
        val photo_tags = findViewById<TextView>(R.id.photo_tags)
//        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo

        val photo = intent.extras!!.getParcelable<Photo>(PHOTO_TRANSFER) as Photo



//        photo_title.text = "Title: " + photo.title
//        photo_tags.text = "Tags: " + photo.tags
        photo_author.text = photo.author
        photo_title.text = resources.getString(R.string.photo_title_text,photo.title)
        photo_tags.text = resources.getString(R.string.photo_tags_text,photo.tags)
//        photo_author.text = resources.getString(R.string.photo_author_text,"my","red","car")

        with(
            Picasso.get()
            .load(photo.link)//loads an image from the url and we store the thumbnail url in the imageField of the photo
            .error(R.drawable.placeholder)//displays when there there is an error in downloading image
            .placeholder(R.drawable.placeholder)//displays while loading the image
            .into(photo_image)) {//this will put the image into the image view widget, int he view holder, this dot into
            this
        }


    }

}















//class PhotoDetailsActivity : AppCompatActivity() {
//
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityPhotoDetailsBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityPhotoDetailsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setSupportActionBar(binding.toolbar)
//
//        val navController = findNavController(R.id.nav_host_fragment_content_photo_details)
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
//        val navController = findNavController(R.id.nav_host_fragment_content_photo_details)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
//}