package com.example.foodwheels.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwheels.databinding.Recycle3viewBinding

class historyadapter(private val buyagain:ArrayList<String>,private val buyagainprice:ArrayList<String>,private val buyagainimg:ArrayList<Int>):RecyclerView.Adapter<historyadapter.historyadapt>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): historyadapt {

        val binding=Recycle3viewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return historyadapt(binding)
    }



    override fun onBindViewHolder(holder: historyadapt, position: Int) {
        holder.bind(position);
    }
    override fun getItemCount(): Int {
       return buyagain.size
    }
    inner class historyadapt(private val binding:Recycle3viewBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
           binding.apply {
               foodnames.text=buyagain[position]
               prices.text=buyagainprice[position]
               imgv.setImageResource(buyagainimg[position])
           }
        }

    }
}