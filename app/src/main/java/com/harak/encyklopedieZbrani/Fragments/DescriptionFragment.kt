package com.harak.encyklopedieZbrani.Fragments

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.google.android.material.textfield.TextInputLayout
import com.harak.encyklopedieZbrani.Common.Common
import com.harak.encyklopedieZbrani.Interface.RetrofitService
import com.harak.encyklopedieZbrani.Model.DetailModel
import com.harak.encyklopedieZbrani.R
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DescriptionFragment : Fragment() {


    lateinit var mService: RetrofitService
    var detailId: Int? = null

    lateinit var detailProgressBar: ProgressBar

    lateinit var descriptionLayout: TextInputLayout
    lateinit var barrelLayout: TextInputLayout
    lateinit var lockLayout: TextInputLayout
    lateinit var setLayout: TextInputLayout
    lateinit var gunStockLayout: TextInputLayout
    lateinit var totalWidthLayout: TextView
    lateinit var barrelWidthLayout: TextView
    lateinit var caliberLayout: TextView
    lateinit var availableUrlLayout: TextInputLayout

    //    lateinit var name : TextView
    lateinit var description: TextView
    lateinit var country: TextView
    lateinit var barrel: TextView
    lateinit var lock: TextView
    lateinit var set: TextView
    lateinit var gunStock: TextView
    lateinit var totalWidth: TextView
    lateinit var barrelWidth: TextView
    lateinit var caliber: TextView
    lateinit var bulletType: TextView
    lateinit var availableUrl: TextView

    lateinit var bulletImage: ImageView

    var url : String? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mService = Common.retrofitService
        // Initialize the TextView
        detailId = arguments?.getInt("id") ?: 0
        detailId?.let { getDetailData(it) }

        detailProgressBar = view.findViewById(R.id.detailProgressBar)

        barrelLayout = view.findViewById(R.id.barrel_TextInputLayout)
        lockLayout = view.findViewById(R.id.lock_TextInputLayout)
        setLayout = view.findViewById(R.id.set_TextInputLayout)
        gunStockLayout = view.findViewById(R.id.gunStock_TextInputLayout)
        descriptionLayout = view.findViewById(R.id.description_TextInputLayout)
        totalWidthLayout = view.findViewById(R.id.totalWidth_textViewLayout)
        barrelWidthLayout = view.findViewById(R.id.barrelWidth_textViewLayout)
        caliberLayout = view.findViewById(R.id.caliber_textViewLayout)
        bulletType = view.findViewById(R.id.bulletType_Textview)
        availableUrlLayout = view.findViewById(R.id.availability_TextInputLayout)

        //  name =  view.findViewById(R.id.name_Textview)
        country = view.findViewById(R.id.country_Textview)
        description = view.findViewById(R.id.description_Textview)
        barrel = view.findViewById(R.id.barrel_Textview)
        lock = view.findViewById(R.id.lock_Textview)
        set = view.findViewById(R.id.set_Textview)
        gunStock = view.findViewById(R.id.gunStock_Textview)
        totalWidth = view.findViewById(R.id.totalWidthValue_textView)
        barrelWidth = view.findViewById(R.id.barrelWidthValue_textView)
        caliber = view.findViewById(R.id.caliberValue_textView)
        availableUrl = view.findViewById(R.id.availability_Textview)

        bulletImage = view.findViewById(R.id.bullet_ImageView)

        availableUrl.setOnClickListener(){
            openWebsite(url)
        }

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_description, container, false)
    }


    private fun getDetailData(id: Int) {

        mService.getDetailData(id,Common.API_KEY).enqueue(object : Callback<DetailModel> {

            override fun onResponse(
                call: Call<DetailModel>,
                response: Response<DetailModel>
            ) {

                if (response.isSuccessful) {
                    val detail: DetailModel? = response.body()
                    if (detail != null) {

                        if (detail.COUNTRY != null) {
                            country.text = detail.COUNTRY
                        } else {
                            country.visibility = View.GONE
                        }

                        if (detail.WIDTH_TOTAL != null) {
                            totalWidth.text = detail.WIDTH_TOTAL + " cm"
                        } else {
                            totalWidthLayout.visibility = View.GONE
                        }

                        if (detail.WIDTH_BARREL != null) {
                            barrelWidth.text = detail.WIDTH_BARREL + " cm"
                        } else {
                            barrelWidthLayout.visibility = View.GONE
                        }

                        if (detail.CALIBER != null) {
                            caliber.text = detail.CALIBER + " mm"
                        } else {
                            caliberLayout.visibility = View.GONE
                        }

                        if (detail.BULLET_IMG != null) {
                            Picasso.get().load(detail.BULLET_IMG).into(bulletImage)
                        } else {
                            bulletImage.visibility = View.GONE
                        }

                        if (detail.BULLET_TYPE != null) {
                            if (detail.CALIBER != null) {
                                bulletType.text = detail.BULLET_TYPE + " " + detail.CALIBER + " mm"
                            } else {
                                bulletType.text = detail.BULLET_TYPE
                            }
                        } else {
                            bulletType.visibility = View.GONE
                        }

                        if (detail.DESCRIPTION_TEXT != null) {
                            description.text = detail.DESCRIPTION_TEXT
                        } else {
                            descriptionLayout.visibility = View.GONE
                        }

                        if (detail.LOCK_TEXT != null) {
                            barrel.text = detail.BARREL_TEXT
                        } else {
                            barrelLayout.visibility = View.GONE
                        }

                        if (detail.LOCK_TEXT != null) {
                            lock.text = detail.LOCK_TEXT
                        } else {
                            lockLayout.visibility = View.GONE
                        }

                        if (detail.SET_TEXT != null) {
                            set.text = detail.SET_TEXT
                        } else {
                            setLayout.visibility = View.GONE
                        }

                        if (detail.GUN_STOCK != null) {
                            gunStock.text = detail.GUN_STOCK
                        } else {
                            gunStockLayout.visibility = View.GONE
                        }

                        if (detail.URL != null) {
                            url = detail.URL
                            val mString = "Navštívit web"
                            val mSpannableString = SpannableString(mString)
                            mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
                            availableUrl.text = mSpannableString

                        } else {
                            availableUrlLayout.visibility = View.GONE
                        }

                        detailProgressBar.visibility = View.GONE
                    }
                } else {
                    // Handle unsuccessful response
                }
            }


            override fun onFailure(call: Call<DetailModel>, t: Throwable) {
                Log.e(ContentValues.TAG, "onResponse: " + t.message)
            }


        })
    }

    private fun openWebsite(url: String?) {
        val context = requireContext()
        if (url != null || url != "null"){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)}
        else {
            Toast.makeText(context,"Produkt není k dispozici",Toast.LENGTH_SHORT).show()
        }
    }



}