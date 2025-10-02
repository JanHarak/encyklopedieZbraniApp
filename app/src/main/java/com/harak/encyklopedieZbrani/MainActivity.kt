package com.harak.encyklopedieZbrani

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputLayout
import com.harak.encyklopedieZbrani.Adapter.MyListAdapter
import com.harak.encyklopedieZbrani.Common.Common
import com.harak.encyklopedieZbrani.Interface.RetrofitService
import com.harak.encyklopedieZbrani.Model.ListModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var mService: RetrofitService

    lateinit var listAdapter: MyListAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var listProgressBar: ProgressBar

    lateinit var countryList: MutableList<ListModel>
    private lateinit var typeTextInputLayout: TextInputLayout
    private lateinit var typeAutoCompleteTextView: AutoCompleteTextView

    private lateinit var countryTextInputLayout: TextInputLayout
    private lateinit var countryAutoCompleteTextView: AutoCompleteTextView
    private val LIST_VIEW = "LIST_VIEW"
    private val GRID_VIEW = "GRID_VIEW"
    var currentView = LIST_VIEW
    var selectedCountryFilter: String = null.toString()
    var selectedTypeFilter: String = null.toString()

    private var isConnected: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mService = Common.retrofitService
        listProgressBar = findViewById(R.id.listProgressBar)
        listProgressBar.visibility = View.VISIBLE
        listAdapter = MyListAdapter(baseContext, mutableListOf())
        countryList = mutableListOf()

        setSupportActionBar(findViewById(R.id.toolbar))


        recyclerView = findViewById(R.id.recyclerViewList)
        recyclerView.setHasFixedSize(true)

        //openDialog()

        getAllListData()


        setListView()

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            getAllListData()
            listAdapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
            countryAutoCompleteTextView.text = null
            typeAutoCompleteTextView.text = null
        }

        typeTextInputLayout = findViewById(R.id.typeTextInputLayout)
        typeAutoCompleteTextView = findViewById(R.id.typeAutoCompleteTextView)
        val typeList = listOf(
            "Vše",
            "Křesadlové pistole",
            "Perkusní pistole",
            "Pistole",
            "Revolvery",
            "Pouze 3D",
            "Dostupné"
        )
        val typeListAdapter = ArrayAdapter(this, R.layout.dropdown_list, typeList)
        typeAutoCompleteTextView.setAdapter(typeListAdapter)
        typeAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->

            selectedTypeFilter = parent.getItemAtPosition(position).toString()
            listAdapter.filterByCombinedData(selectedTypeFilter, selectedCountryFilter)
        }


        countryTextInputLayout = findViewById(R.id.countryTextInputLayout)
        countryAutoCompleteTextView = findViewById(R.id.countryAutoCompleteTextView)
        val countryList =
            listOf(
                "Vše",
                "Anglie",
                "Belgie",
                "Čechy",
                "Evropa",
                "Dánsko",
                "Francie",
                "Irsko",
                "Itálie",
                "Japonsko",
                "Kavkaz",
                "Německo",
                "Persie",
                "Polsko",
                "Prusko",
                "Rakousko",
                "Sasko",
                "Španělsko",
                "Švédsko",
                "Uhry",
                "Ukrajina",
                "USA"
            )
        val countryListAdapter = ArrayAdapter(this, R.layout.dropdown_list, countryList)
        countryAutoCompleteTextView.setAdapter(countryListAdapter)

        countryAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->

            selectedCountryFilter = parent.getItemAtPosition(position).toString()

            listAdapter.filterByCombinedData(selectedTypeFilter, selectedCountryFilter)

        }
    }


    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        val searchView: SearchView? = menu?.findItem(R.id.search)!!.actionView as SearchView?
        searchView?.queryHint = resources.getString(R.string.search_hint)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                listAdapter.filter.filter(query)
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter.filter.filter(newText)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.switchLayout -> {

                if (currentView == LIST_VIEW) {
                    listAdapter.switchLayout(MyListAdapter.LayoutType.GRID)
                    setGridView()
                    item.title = "Seznam"
                    item.setIcon(R.drawable.baseline_view_list_24)
                    println("currentView is" + currentView)
                } else {
                    listAdapter.switchLayout(MyListAdapter.LayoutType.LIST)
                    setListView()

                    item.title = "Mřížka"
                    item.setIcon(R.drawable.baseline_grid_view_24)
                    println("currentView is" + currentView)
                }
                recyclerView.adapter = listAdapter

                true

            }

            R.id.aboutApp -> {
                openDialog()

                Toast.makeText(this, "Vyrobil: Jan Harák", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setListView() {
        currentView = LIST_VIEW
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setGridView() {
        currentView = GRID_VIEW
        recyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    private fun setUpAdapterLayout() {
        if (currentView == LIST_VIEW) {
            listAdapter.switchLayout(MyListAdapter.LayoutType.LIST)
        } else {
            listAdapter.switchLayout(MyListAdapter.LayoutType.GRID)
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private fun getAllListData() {
        isConnected = isNetworkConnected()
        if (isConnected) {

            mService.getListData(Common.API_KEY).enqueue(object : Callback<MutableList<ListModel>> {
                override fun onResponse(
                    call: Call<MutableList<ListModel>>,
                    response: Response<MutableList<ListModel>>
                ) {
                    val listDataIn = response.body()!! as MutableList<ListModel>
                    listAdapter = MyListAdapter(baseContext, listDataIn)
                    setUpAdapterLayout()
                    listAdapter.notifyDataSetChanged()
                    recyclerView.adapter = listAdapter
                    listProgressBar.visibility = View.GONE
                    println("DATA " + listDataIn)

                }


                override fun onFailure(call: Call<MutableList<ListModel>>, t: Throwable) {
                    Log.e(TAG, "onResponse: " + t.message)
                }
            })
        } else {
            Toast.makeText(this, "Internetové připojení není k dispozici", Toast.LENGTH_SHORT)
                .show()
            listProgressBar.visibility = View.GONE
        }
    }

    private fun openDialog() {

        val url = Common.WEB_URL

        val customDialog = LayoutInflater.from(this).inflate(R.layout.about_app_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(customDialog)
            .setTitle("O aplikaci")
            .setPositiveButton("Zavřít") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("Navštívit web") { dialog, _ ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        dialogBuilder.show()
    }
}

