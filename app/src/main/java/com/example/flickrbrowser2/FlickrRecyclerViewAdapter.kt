package com.example.flickrbrowser2

import android.service.controls.templates.ThumbnailTemplate
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FlickrImageViewHolder(view: View) : RecyclerView.ViewHolder(view){
    var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    var title: TextView = view.findViewById<TextView>(R.id.title2)
}
class FlickrRecyclerViewAdapter(private var photoList : List<Photo>) : RecyclerView.Adapter<FlickrImageViewHolder>() {
    private val TAG = "FlickrRecyclerViewAdapt"
    override fun getItemCount(): Int {
//        Log.d(TAG,".getItemCount called")
        return if(photoList.isNotEmpty()) photoList.size else 1//if no photos match the tags we search for, we get a placeholder image and a message.placeholder is a image which is showed when loading
    }
    fun loadNewData(newPhoto: List<Photo>){//this function takes the new list as parameter and stores it in photoList and notifies the recyclerView and then it will refresh to get new data
        photoList = newPhoto
        notifyDataSetChanged()
    }
    fun getPhoto(position: Int): Photo?{
        return if (photoList.isNotEmpty()) photoList[position] else null
    }
    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {//this method is called when a recycler view wants new data to be stored in a view holder
        //called by the layout manger when it wants new data in an existing view

        if (photoList.isEmpty()){
            holder.thumbnail.setImageResource(R.drawable.placeholder)
            holder.title.setText(R.string.empty_photo)
        }else{
            val photoItem = photoList[position]//retrieve the position from the photoList object
//        Log.d(TAG,"onBindViewHolder: ${photoItem.title} --> $position")
            //when the below function finished then the recycler view can display the holder in its list
            with(Picasso.get()
                .load(photoItem.image)//loads an image from the url and we store the thumbnail url in the imageField of the photo
                .error(R.drawable.placeholder)//displays when there there is an error in downloading image
                .placeholder(R.drawable.placeholder)//displays while loading the image
                .into(holder.thumbnail)) {//this will put the image into the image view widget, int he view holder, this dot into
                holder.thumbnail.context
            }
            holder.title.text = photoItem.title
        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        //called by the layout manager when it needs a new view
        Log.d(TAG,".onCreateViewHolder new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse,parent,false)//false means it tell not to attach the inflated view to parent
        return FlickrImageViewHolder(view)
    }
}