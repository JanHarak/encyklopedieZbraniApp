package com.harak.encyklopedieZbrani.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.harak.encyklopedieZbrani.R
import com.squareup.picasso.Picasso
import java.lang.Float.max
import java.lang.Float.min


class MyFullscreenPagerAdapter(private val context: Context, private val imageUrls: List<String>) :
    RecyclerView.Adapter<MyFullscreenPagerAdapter.ImageViewHolder>() {

    private lateinit var mScaleGestureDetector: ScaleGestureDetector
    private var mScaleFactor = 1.0f
    private lateinit var holder: ImageViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fullscreen_image_item, parent, false)
        return ImageViewHolder(view)

    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        if (imageUrl != null || imageUrl != "NULL") {
            Picasso.get().load(imageUrl).into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.photo_view)

    }

}