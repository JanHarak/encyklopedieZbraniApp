package com.harak.encyklopedieZbrani.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.harak.encyklopedieZbrani.DetailActivity
import com.harak.encyklopedieZbrani.Model.ListModel
import com.harak.encyklopedieZbrani.R
import com.squareup.picasso.Picasso
import java.security.AlgorithmConstraints
import java.util.Locale


class MyListAdapter(
    private val context: Context,
    private var itemModelList: MutableList<ListModel>
) :
    RecyclerView.Adapter<MyListAdapter.MyViewHolder>(), Filterable {

    private var filteredDataList: MutableList<ListModel> = itemModelList.toMutableList()
    private var filtered3dDataList: MutableList<ListModel> = itemModelList.toMutableList()

    private var currentLayoutType: LayoutType = LayoutType.LIST

    enum class LayoutType {
        LIST,
        GRID
    }


   inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id: Int? = null
        var listName: TextView
        var listImg: ImageView
        var listStock: ImageView
        var list3d: ImageView
        var listCountry: TextView

        val shadowLayout: RelativeLayout = itemView.findViewById(R.id.shadowLayout)

       init {
            listName = itemView.findViewById(R.id.nameList)
            listCountry = itemView.findViewById(R.id.countryList)
            listImg = itemView.findViewById(R.id.imgList)
            listStock = itemView.findViewById(R.id.isStockAvailableIcon)
            list3d = itemView.findViewById(R.id.is3dAvailableIcon)

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val layoutResId = if (currentLayoutType == LayoutType.LIST) {
            R.layout.list_item
        } else {
            R.layout.grid_item
        }

        return MyViewHolder(LayoutInflater.from(context).inflate(layoutResId, parent, false))
    }

    override fun getItemCount(): Int {
        return filteredDataList.size
    }

    override fun onBindViewHolder( holder: MyViewHolder, position: Int) {

        Picasso.get().load(filteredDataList[position].IMG_1).into(holder.listImg)

        holder.listName.text = filteredDataList[position].LIST_NAME
        holder.id = filteredDataList[position].LIST_ID
        holder.listCountry.text = filteredDataList[position].LIST_COUNTRY
        holder.listStock.isVisible = filteredDataList[position].LIST_AVAILABLE != 0
        holder.list3d.isVisible = filteredDataList[position].LIST_3D_MODEL != 0
        holder.shadowLayout.visibility = View.GONE

//        holder.itemView.isEnabled = filteredDataList[position].LIST_AVAILABLE == 1
//        holder.itemView.isActivated = filteredDataList[position].LIST_AVAILABLE == 1
//        holder.itemView.isClickable = filteredDataList[position].LIST_AVAILABLE == 1
//        holder.itemView.isContextClickable = filteredDataList[position].LIST_AVAILABLE == 1

//        if (filteredDataList[position].LIST_ACCESS == 1) {
//            holder.itemView.isEnabled = true
//            holder.shadowLayout.visibility = View.GONE
//        } else {
//            holder.itemView.isEnabled = false
//            holder.shadowLayout.visibility = View.VISIBLE
//        }


     //   if (filteredDataList[position].LIST_ACCESS == 1) {
            holder.itemView.setOnClickListener {
                // Handle item click event
                val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("itemId", holder.id)
                intent.putExtra("nameId", holder.listName.text)
                if (filteredDataList[position].LIST_3D_MODEL == 1) {
                    intent.putExtra("modelAvailAble", holder.id)
                }
                holder.itemView.context.startActivity(intent)
            }
        }
   // }

    fun switchLayout(layoutType: LayoutType) {
        currentLayoutType = layoutType
        notifyDataSetChanged()
    }


    // Function to filter the data based on the value of LIST_3D_MODEL
    fun filterAllData(){
        filteredDataList = itemModelList.toMutableList()
        notifyDataSetChanged()
    }
    fun filterBy3DModelValue(){
        filteredDataList= itemModelList.filter { it.LIST_3D_MODEL == 1 }.toMutableList()

        notifyDataSetChanged()
    }

    fun filterByAvailabibility(){
        filteredDataList= itemModelList.filter { it.LIST_AVAILABLE == 1 }.toMutableList()

        notifyDataSetChanged()
    }

    fun filterByTypeValue(type: String){
        filteredDataList= itemModelList.filter { it.LIST_TYPE == type }.toMutableList()
        notifyDataSetChanged()
    }

    fun filterByCountryValue(country: String){
        filteredDataList= itemModelList.filter { it.LIST_COUNTRY?.contains(country) == true }.toMutableList()
        notifyDataSetChanged()

    }

    fun filterByFullAccess(){
        filteredDataList= itemModelList.filter { it.LIST_ACCESS == 1 }.toMutableList()
        notifyDataSetChanged()

    }

    fun filterByCombinedData(type: String,country: String){

        if ((country == "null" || country == "Vše") &&  (type == "null" || type == "Vše")){
            filterAllData()
        }
        else if (type == "Pouze 3D"){
            println("ADAPTER TYPE " + type + "COUNTRY +" + country)
            if(country == "null" || country == "Vše") {
                filterBy3DModelValue()
            }
            else{filteredDataList =
                itemModelList.filter { it.LIST_COUNTRY?.contains(country) == true && it.LIST_3D_MODEL == 1 }
                    .toMutableList()
                notifyDataSetChanged()}
        }

        else if (type == "Dostupné"){
            println("ADAPTER TYPE " + type + "COUNTRY +" + country)
            if(country == "null" || country == "Vše") {
                filterByAvailabibility()
            }
            else{filteredDataList =
                itemModelList.filter { it.LIST_COUNTRY?.contains(country) == true && it.LIST_AVAILABLE == 1 }
                    .toMutableList()
                notifyDataSetChanged()}
        }

        else if (type == "null" || type == "Vše"){
            filterByCountryValue(country)
        }
        else if (country == "null" || country == "Vše"){
            filterByTypeValue(type)
        }

        else{
            filteredDataList= itemModelList.filter { it.LIST_COUNTRY?.contains(country) == true && it.LIST_TYPE == type }.toMutableList()
            notifyDataSetChanged()
        }

    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val filteredList: ArrayList<ListModel> = ArrayList()
                val query = constraint.toString().lowercase(Locale.getDefault()).trim()

                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(itemModelList)

                } else {
                    for (data in itemModelList) {
                        if (data.LIST_NAME?.lowercase(Locale.getDefault())!!.contains(query)) {
                            filteredList.add(data)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredDataList.clear()
                filteredDataList = results.values as MutableList<ListModel>
                notifyDataSetChanged()
            }
        }
    }

}