package com.harak.encyklopedieZbrani.Fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harak.encyklopedieZbrani.Adapter.MyImageAdapter
import com.harak.encyklopedieZbrani.Common.Common
import com.harak.encyklopedieZbrani.Interface.RetrofitService
import com.harak.encyklopedieZbrani.Model.ImagesModel
import com.harak.encyklopedieZbrani.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GalleryFragment : Fragment() {

    lateinit var mService: RetrofitService
    var detailId: Int? = null
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar

    lateinit var imageList: ArrayList<String>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.image_recyclerView)
        progressBar = view.findViewById(R.id.imageProgressBar)
        progressBar.visibility = View.VISIBLE


        mService = Common.retrofitService
        // Initialize the TextView
        detailId = arguments?.getInt("id") ?: 0
        getAllImagesData(detailId!!)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    private fun getAllImagesData(id: Int) {

        mService.getImageData(id).enqueue(object : Callback<ImagesModel> {

            override fun onResponse(
                call: Call<ImagesModel>,
                response: Response<ImagesModel>
            ) {

                if (response.isSuccessful) {
                    val images: ImagesModel? = response.body()
                    println("IMAGE FROM RESPONSE " + images)
                    if (images != null) {

                        imageList = ArrayList()

                        if(images?.IMG_1 != null){
                            imageList.add(images?.IMG_1.toString())
                        }

                        if(images?.IMG_2 != null){
                            imageList.add(images?.IMG_2.toString())
                        }
                        if(images?.IMG_3 != null){
                            imageList.add(images?.IMG_3.toString())
                        }
                        if(images?.IMG_4 != null){
                            imageList.add(images?.IMG_4.toString())
                        }
                        if(images?.IMG_5 != null){
                            imageList.add(images?.IMG_5.toString())
                        }
                        if(images?.IMG_6 != null){
                            imageList.add(images?.IMG_6.toString())
                        }

                    }
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerView.adapter =
                            MyImageAdapter(
                                requireContext(), imageList
                            )
                        progressBar.visibility = View.GONE

                    }
                 else {
                    // Handle unsuccessful response
                }
            }


            override fun onFailure(call: Call<ImagesModel>, t: Throwable) {
                Log.e(ContentValues.TAG, "onResponse: " + t.message)
            }


        })
    }

}
