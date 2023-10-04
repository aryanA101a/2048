package com.example.a2048.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.a2048.R
import com.example.a2048.databinding.GridItemBinding
import com.example.a2048.model.Cell
import com.example.a2048.util.Utils

class GridAdapter() : ListAdapter<Cell, MyViewHolder>(MyDiffCallback()) {

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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val coordinates = Utils.idxToCoordinates(position)
        holder.bind(getItem(position))
    }


}

class MyViewHolder(val binding: GridItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Cell) {
        binding.cellTextView.text = data.value.toString()
        binding.cellTextView.visibility = if (data.value == 0) View.INVISIBLE else View.VISIBLE
    }

}

class MyDiffCallback : DiffUtil.ItemCallback<Cell>() {
    override fun areItemsTheSame(
        oldItem: Cell,
        newItem: Cell
    ): Boolean {
//        Log.i("position", "areItemsTheSame: old:${oldItem.position} new:${newItem.position}")
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Cell,
        newItem: Cell
    ): Boolean {
//        Log.i("value", "areContentsTheSame: old:${oldItem.value} new:${newItem.value}")
        return oldItem.value == newItem.value
    }

}

class GridItemAnimator() : DefaultItemAnimator() {
    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder?,
        newHolder: RecyclerView.ViewHolder?,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
//        if (fromX < toX) {
//            if (oldHolder!=newHolder){
//                oldHolder?.let { dispatchAnimationFinished(it) }
//            }
//            newHolder?.let { dispatchAnimationFinished(it) }
//            return false
//        }
        return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY)
    }


}