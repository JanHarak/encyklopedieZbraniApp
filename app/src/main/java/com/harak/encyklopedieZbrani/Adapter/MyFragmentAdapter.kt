package com.harak.encyklopedieZbrani.Adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.harak.encyklopedieZbrani.Fragments.DescriptionFragment
import com.harak.encyklopedieZbrani.Fragments.GalleryFragment
import com.harak.encyklopedieZbrani.Model.DetailModel
import com.harak.encyklopedieZbrani.Model.ListModel

class MyFragmentAdapter(
    fm: FragmentActivity,
    var dataId: Int
) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = DescriptionFragment()
                fragment.arguments = Bundle().apply {
                    putInt("id", dataId)
                }
                fragment
            }

            1 -> {
             val fragment = GalleryFragment()
                fragment.arguments = Bundle().apply {
                    putInt("id", dataId)
                }
                fragment
            }
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
