package com.aryan.a2048.adapter

import android.content.res.ColorStateList
import android.graphics.Color.parseColor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aryan.a2048.R
import com.aryan.a2048.data.model.Cell
import com.aryan.a2048.databinding.CellBinding
import com.aryan.a2048.util.Utils
import kotlin.math.log2

class GridAdapter() : ListAdapter<Cell, MyViewHolder>(MyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.cell,
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

class MyViewHolder(val binding: CellBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Cell) {

binding.cellTextView.setTextColor(getTextColorFromCellValue(data.value))
            binding.cell.backgroundTintList =
                ColorStateList.valueOf(getColorFromCellValue(data.value))
        binding.cellTextView.text = data.value.toString()
        binding.cellTextView.visibility = if (data.value == 0) View.INVISIBLE else View.VISIBLE
    }

    private fun getColorFromCellValue(value: Int): Int {
        return when (value) {
            0 -> parseColor("#CDC1B3")
            2 -> parseColor("#EFE4DB")
            4 -> parseColor("#ECE0C9")
            8 -> parseColor("#F3B079")
            16 -> parseColor("#F59462")
            32 -> parseColor("#F77C5E")
            64 -> parseColor("#F65E3B")
            128 -> parseColor("#EDD050")
            256 -> parseColor("#EDC53F")
            512 -> parseColor("#EDC050")
            1024 -> parseColor("#EDB53F")
            2048 -> parseColor("#ECA53F")
            4096 -> parseColor("#B685AB")
            8192 -> parseColor("#B665AB")
            16384 -> parseColor("#AB61A6")
            else -> parseColor("#18DC69")
        }
    }

    private fun getTextColorFromCellValue(value: Int): Int {
        return when (value) {
            8,16,32,64,256,512,1024,2048,8192,16384 -> parseColor("#FFFFFDE2")
            else -> parseColor("#1E1E1E")
        }
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