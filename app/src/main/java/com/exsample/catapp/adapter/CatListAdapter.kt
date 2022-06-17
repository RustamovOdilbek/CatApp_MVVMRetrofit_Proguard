package com.exsample.catapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exsample.catapp.databinding.ItemCatViewBinding
import com.exsample.catapp.models.CatItem
import com.exsample.catapp.util.OnClickEvent

/**
 * in this adapter, we print the car data
 */
class CatListAdapter(var cats: ArrayList<CatItem>,  var onClickEvent: OnClickEvent): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemCatViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cat = cats[position]
        if (holder is CatViewHolder){
            holder.view.apply {
                if (cat.image != null) {
                    Glide.with(holder.view.ivImage)
                        .load(cat.image.url)
                        .into(holder.view.ivImage)
                }
                tvName.text = cat.name
                tvDescription.text = cat.description

                btnDownload.setOnClickListener { onClickEvent.setOnButtomClick(ivImage) }
            }
        }
    }

    override fun getItemCount(): Int = cats.size

    class CatViewHolder(val view: ItemCatViewBinding) : RecyclerView.ViewHolder(view.root)
}