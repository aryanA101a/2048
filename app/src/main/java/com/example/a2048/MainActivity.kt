package com.example.a2048

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.a2048.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;
    private lateinit var viewModel: GameViewModel
    private lateinit var gridAdapter: GridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)



        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        initGrid()


    }

    private fun initGrid() {
        binding.grid.setBackgroundColor(resources.getColor(R.color.Board))
        binding.grid.layoutManager = GridLayoutManager(this, 4)
        gridAdapter = GridAdapter(viewModel.gameState.value!!.toMutableList())
        binding.grid.adapter=gridAdapter
        Log.i("billi", "initGrid: ${viewModel.gameState.value!!.toMutableList().toString()}")


        displayGameBoard()
    }

    private fun displayGameBoard() {
        viewModel.gameState.observe(this) {
            gridAdapter.setState(it)
            gridAdapter.notifyDataSetChanged()
        }
    }
}