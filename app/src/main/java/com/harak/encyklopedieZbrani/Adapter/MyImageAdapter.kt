package com.harak.encyklopedieZbrani.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.harak.encyklopedieZbrani.FullscreenActivity
import com.harak.encyklopedieZbrani.R
import com.squareup.picasso.Picasso

class MyImageAdapter(private val context: Context, private val imageList: ArrayList<String>) :
    RecyclerView.Adapter<MyImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        println("IMAGES Z ADAPTERU" + imageList)

        val imageModel = imageList[position]
        Picasso.get().load(imageModel).into(holder.imageView)

        holder.imageView.setOnClickListener {
            val intent = Intent(context, FullscreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("image", imageModel)
            intent.putStringArrayListExtra("imageUrls", ArrayList(imageList))
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
        

    }


    override fun getItemCount(): Int {
        return imageList.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

    }
}