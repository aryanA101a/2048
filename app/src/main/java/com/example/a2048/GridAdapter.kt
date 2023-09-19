package com.example.a2048

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.a2048.databinding.GridItemBinding

class GridAdapter(private var  gridState:MutableList<Int>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.grid_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return gridState.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bind(gridState[position])
    }

    fun setState(state:List<Int>){
        gridState.clear()
        gridState.addAll(state)
    }

}

class MyViewHolder(val binding: GridItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(value:Int){
        if (value!=0){
            binding.cellTextView.text=value.toString()
        }
    }

}