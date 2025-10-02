package com.harak.encyklopedieZbrani

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.harak.encyklopedieZbrani.Adapter.MyFragmentAdapter
import com.harak.encyklopedieZbrani.Common.Common
import com.harak.encyklopedieZbrani.Fragments.DescriptionFragment
import com.harak.encyklopedieZbrani.Interface.RetrofitService
import com.harak.encyklopedieZbrani.Model.DetailModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: TextView

    var detailId : Int? = null
    var nameId : String? = null
    var model3dAvailAble : Int? = null

    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        detailId = intent.getIntExtra("itemId", -1)
        nameId = intent.getStringExtra("nameId")
        model3dAvailAble = intent.getIntExtra("modelAvailAble", 0)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)
        toolbar = findViewById(R.id.toolbar_detail)

        toolbar.text = nameId

        viewPager.adapter = MyFragmentAdapter(this, detailId!!)

        fab = findViewById(R.id.fab)
        if (model3dAvailAble == 0){
            fab.visibility = View.GONE
        }else {
            fab.visibility = View.VISIBLE
        }

        fab.setOnClickListener {
            val intent = Intent(this, ThreeDimensionalModelActivity::class.java)
            intent.putExtra("itemId", detailId)
            this.startActivity(intent)
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Popis"
                1 -> "Galerie"
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }.attach()

    }



}


