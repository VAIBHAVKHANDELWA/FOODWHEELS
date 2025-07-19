package com.example.foodwheels.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodwheels.databinding.ViewMenuItemBinding
import com.example.foodwheels.itemdetails
import com.example.foodwheels.model1

class menuadapter (private val menuitem:List<model1>, private val requirecontext:Context):RecyclerView.Adapter<menuadapter.menuViewHolder>() {
private val itemclick: View.OnClickListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val binding =
            ViewMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return menuViewHolder(binding)
    }


    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return menuitem.size
    }

    inner class menuViewHolder(private val binding: ViewMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        openactivity(position)

                    }

                }
            }
        fun bind(position: Int) {
            binding.apply {
                val menu=menuitem[position]
                itempricepop.text = menu.foodprice
                itemnamepop.text = menu.foodname
                val Uri: Uri = Uri.parse(menu.foodimage)
                 Glide.with(requirecontext).load(Uri).into(itemimagepop)
            }




        }


    }

    private fun openactivity(position: Int) {
        val menu=menuitem[position]
        val intent = Intent(requirecontext, itemdetails::class.java).apply {
            putExtra("itemname", menu.foodname)
           putExtra("itemimage", menu.foodimage)
           putExtra("itemprice", menu.foodprice)
            putExtra("itemdescription", menu.foodndescrip)
           putExtra("itemrating", menu.ingridients)

        }
        requirecontext.startActivity(intent)

    }
}

private fun View.OnClickListener?.onItemClick(position: Int) {


}
