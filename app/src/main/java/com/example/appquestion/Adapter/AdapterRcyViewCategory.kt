package com.example.appquestion.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appquestion.Acivity.ScreenDifficulty
import com.example.appquestion.Model.Categories
import com.example.appquestion.R
import kotlinx.android.synthetic.main.item_screen_category.view.*

class AdapterRcyViewCategory(private val conText:Context): RecyclerView.Adapter<AdapterRcyViewCategory.ViewHolder>() {
    private lateinit var dataList:ArrayList<Categories.trivia_categories>

    fun senData(dataList: ArrayList<Categories.trivia_categories>){
        this.dataList = dataList;
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val btn = itemView.item_screen_category_btn;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(conText).inflate(R.layout.item_screen_category,parent,false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.btn.setText(dataList.get(position).name);

        holder.btn.setOnClickListener {
            var bundle = Bundle()
            var intent = Intent(conText,ScreenDifficulty::class.java)
            bundle.putSerializable("Category",dataList.get(position))
            intent.putExtras(bundle);
            conText.startActivity(intent);
        }
    }

    override fun getItemCount(): Int {
        if (dataList==null){
            return 0;
        }
        return dataList!!.size;
    }


}