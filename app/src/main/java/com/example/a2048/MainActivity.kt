package com.example.a2048

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.a2048.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;
    private lateinit var viewModel: GameViewModel
    private lateinit var gridAdapter: GridAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.cLayout.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {
            override fun onSwipeLeft() {
                viewModel.onLeft(gridAdapter)
            }

            override fun onSwipeRight() {
                super.onSwipeRight()

            }

            override fun onSwipeUp() {
                viewModel.placeRandomCell()

            }

            override fun onSwipeDown() {

            }
        })
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        initGrid()


    }


    private fun initGrid() {
        binding.grid.setBackgroundColor(resources.getColor(R.color.Board))
        binding.grid.layoutManager = GridLayoutManager(this, 4)
//        binding.grid.itemAnimator = GridItemAnimator(50L)
        gridAdapter = GridAdapter()
        binding.grid.adapter = gridAdapter
        Log.i("TAG", "initGrid:${viewModel.boardState.value!!} ")
        gridAdapter.submitList(viewModel.boardState.value!!.flatten())

        viewModel.placeRandomCell()
        viewModel.placeRandomCell()

        displayGameBoard()
    }

    private fun displayGameBoard() {
        viewModel.boardState.observe(this) {
            Log.i("TAG", "displayGameBoard: $it")
            gridAdapter.submitList(it.flatten())
        }
    }


}