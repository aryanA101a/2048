package com.example.a2048

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.a2048.adapter.GridAdapter
import com.example.a2048.adapter.GridItemAnimator
import com.example.a2048.databinding.ActivityMainBinding
import com.example.a2048.view.OnSwipeTouchListener
import com.example.a2048.viewmodel.GameViewModel
import kotlinx.coroutines.launch

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
                lifecycleScope.launch {
                    viewModel.onSwipe(GameViewModel.SwipeDirection.LEFT)
                }
            }
            override fun onSwipeRight() {
                lifecycleScope.launch {
                    viewModel.onSwipe(GameViewModel.SwipeDirection.RIGHT)
                }

            }
            override fun onSwipeUp() {
                viewModel.onSwipe(GameViewModel.SwipeDirection.UP)

            }
            override fun onSwipeDown() {
                viewModel.onSwipe(GameViewModel.SwipeDirection.DOWN)
            }
        })
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        initGrid()


    }


    private fun initGrid() {
        binding.grid.setBackgroundColor(resources.getColor(R.color.Board))
        binding.grid.layoutManager = GridLayoutManager(this, 4)
        binding.grid.itemAnimator = GridItemAnimator()
        gridAdapter = GridAdapter()
        binding.grid.adapter = gridAdapter
        Log.i("TAG", "initGrid:${viewModel.gameBoardState.value!!} ")
        gridAdapter.submitList(viewModel.gameBoardState.value!!.flatten())

        displayGameBoard()
    }

    private fun displayGameBoard() {
        viewModel.gameBoardState.observe(this) {
//            Log.i("TAG", "displayGameBoard: $it")
            gridAdapter.submitList(it.flatten())
        }
    }


}