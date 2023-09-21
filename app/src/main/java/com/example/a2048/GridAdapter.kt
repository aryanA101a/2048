package com.example.a2048

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.a2048.databinding.GridItemBinding

class GridAdapter() : ListAdapter<Util.PositionedInt, MyViewHolder>(MyDiffCallback()) {

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
        val coordinates = Util.idxToCoordinates(position)
        holder.bind(getItem(position))
    }


}

class MyViewHolder(val binding: GridItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Util.PositionedInt) {
        if (data.value != 0) {
            binding.cellTextView.text = data.value.toString()
        }
    }

}

class MyDiffCallback : DiffUtil.ItemCallback<Util.PositionedInt>() {
    override fun areItemsTheSame(
        oldItem: Util.PositionedInt,
        newItem: Util.PositionedInt
    ): Boolean {
        return oldItem.position == newItem.position
    }

    override fun areContentsTheSame(
        oldItem: Util.PositionedInt,
        newItem: Util.PositionedInt
    ): Boolean {
        return oldItem.value == newItem.value
    }

}

class GridItemAnimator(private val duration: Long) : DefaultItemAnimator() {

    override fun getAddDuration(): Long {
        return duration
    }

    override fun getRemoveDuration(): Long {
        return duration
    }

    override fun getMoveDuration(): Long {
        return duration
    }

    override fun getChangeDuration(): Long {
        return duration
    }
}