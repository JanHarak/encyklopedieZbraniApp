package com.harak.encyklopedieZbrani

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.harak.encyklopedieZbrani.Adapter.MyFullscreenPagerAdapter
import com.harak.encyklopedieZbrani.databinding.ActivityFullscreenBinding

class FullscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var imageUrls: List<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loadedImage = intent.getStringExtra("image")
        imageUrls = intent.getStringArrayListExtra("imageUrls") ?: emptyList()

        val adapter = MyFullscreenPagerAdapter(this, imageUrls)
        // Initialize the ViewPager2
        viewPager = binding.viewPager
        viewPager.adapter = adapter //MyFullscreenPagerAdapter(this,imageUrls)


        // Set the starting position for the ViewPager2
        val startPosition = intent.getIntExtra("position", 0)
        viewPager.setCurrentItem(startPosition, true)
    }
}


