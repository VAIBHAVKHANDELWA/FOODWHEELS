package com.example.foodwheels.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodwheels.databinding.ItemlistforrecycleBinding
import com.example.foodwheels.itemdetails
import com.example.foodwheels.model1

class popularitems(
    private val items: List<model1>,
    private val requirecontext: Context
) : RecyclerView.Adapter<popularitems.popularitemviewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): popularitemviewholder {
        return popularitemviewholder(
            ItemlistforrecycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: popularitemviewholder, position: Int) {
        val item = items[position]
        holder.bind(item, requirecontext)

        holder.itemView.setOnClickListener {
            val intent = Intent(requirecontext, itemdetails::class.java)
            intent.putExtra("itemname", item.foodname)
            intent.putExtra("itemimage", item.foodimage) // Send URL
            intent.putExtra("itemprice", item.foodprice)
            intent.putExtra("itemdescription", item.foodndescrip)
            intent.putExtra("itemingredients", item.ingridients)
            requirecontext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    class popularitemviewholder(private val binding: ItemlistforrecycleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: model1, context: Context) {
            binding.itemName.text = item.foodname
            binding.itemPrice.text = item.foodprice
            Glide.with(context)
                .load(item.foodimage)
                .into(binding.itemImage)
        }
    }
}
