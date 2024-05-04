//package com.example.flickrbrowser2
//
//import android.util.Log
//import java.io.IOException
//import java.io.ObjectStreamException
//import java.io.Serializable
//
//import android.os.Parcelable
//
//class Photo(
//    var title: String, var author: String, var authorID: String,
//    var link: String, var tags: String,
//    var image: String
//) : Serializable {//Serialization is the process of converting data used by an application to a format that can be transferred over a network or stored in a database or a file.
//
//    companion object {
//        private const val serialVersionUID = 1L
//    }
//
//    override fun toString(): String {
//        return "Photo(title='$title', author='$author', authorID='$authorID', link='$link', tags='$tags', image='$image')"
//    }
//
//    @Throws(IOException::class)
//    private fun writeObject(out: java.io.ObjectOutputStream) {//to
//        Log.d("Photo", "writeObject called")
//        out.writeUTF(title)//to write the string
//        out.writeUTF(author)
//        out.writeUTF(authorID)
//        out.writeUTF(link)
//        out.writeUTF(tags)
//        out.writeUTF(image)
//    }
//
//    @Throws(IOException::class, ClassNotFoundException::class)
//    private fun readObject(inStream: java.io.ObjectInputStream) {
//        Log.d("Photo", "readObject called")
//        title = inStream.readUTF()
//        author = inStream.readUTF()
//        authorID = inStream.readUTF()
//        link = inStream.readUTF()
//        tags = inStream.readUTF()
//        image = inStream.readUTF()
//    }
//
//    @Throws(ObjectStreamException::class)
//    private fun readObjectNoData() {
//        Log.d("Photo", "readObjectNoData called")
//    }
//}

/*******USING PARCELABLE*******/

package com.example.flickrbrowser2

import android.os.Parcel

import android.os.Parcelable

class Photo(
    var title: String?, var author: String?, var authorID: String?,
    var link: String?, var tags: String?,
    var image: String?
) : Parcelable {//Serialization is the process of converting data used by an application to a format that can be transferred over a network or stored in a database or a file.


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun toString(): String {
        return "Photo(title='$title', author='$author', authorID='$authorID', link='$link', tags='$tags', image='$image')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(authorID)
        parcel.writeString(link)
        parcel.writeString(tags)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }
}