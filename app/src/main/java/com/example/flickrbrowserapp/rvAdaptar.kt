package com.example.flickrbrowserapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flickrbrowserapp.databinding.ItemRowBinding


data class Image(var title: String, var link: String)

class rvAdaptar (val acticity: MainActivity,val Img:ArrayList<Image>):RecyclerView.Adapter<rvAdaptar.ItemHolder>(){

    class ItemHolder (val binding:ItemRowBinding):RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(ItemRowBinding.inflate(LayoutInflater.from(
            parent.context
        ),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val photo = Img[position]
        holder.binding.apply {
            txt1.text = photo.title
            Glide.with(acticity).load(photo.link).into(imageView)
            ItemRow.setOnClickListener { acticity.openImag(photo.link) }

        }
    }

    override fun getItemCount()= Img.size

}