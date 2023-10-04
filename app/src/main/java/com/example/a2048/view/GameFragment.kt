package com.example.a2048.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.a2048.R
import com.example.a2048.adapter.GridAdapter
import com.example.a2048.adapter.GridItemAnimator
import com.example.a2048.databinding.FragmentGameBinding
import com.example.a2048.util.Direction
import com.example.a2048.util.MoveOutcome
import com.example.a2048.viewmodel.GameViewModel
import kotlinx.coroutines.launch


class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private lateinit var gridAdapter: GridAdapter
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false).apply {
            viewModel = gameViewModel
            lifecycleOwner = viewLifecycleOwner

            btnReset.setOnClickListener {
                handleResetGame()
            }

            clFragmentGame.setOnTouchListener(object :
                OnSwipeTouchListener(requireContext()) {

                override fun onSwipeLeft() {
                    gameViewModel.onSwipe(Direction.LEFT)
                }

                override fun onSwipeRight() {
                    gameViewModel.onSwipe(Direction.RIGHT)

                }

                override fun onSwipeUp() {
                    gameViewModel.onSwipe(Direction.UP)

                }

                override fun onSwipeDown() {
                    gameViewModel.onSwipe(Direction.DOWN)
                }
            })


        }
        initGrid()
        displayGameBoard()


        return binding.root

    }

    private fun initGrid() {
        binding.rvGrid.layoutManager = object : GridLayoutManager(requireContext(), 4) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.rvGrid.itemAnimator = GridItemAnimator()
        gridAdapter = GridAdapter()
        binding.rvGrid.adapter = gridAdapter
        Log.i("TAG", "initGrid:${gameViewModel.gameBoardState.value!!} ")
        gridAdapter.submitList(gameViewModel.gameBoardState.value!!.cellMatrix.flatten())
    }

    private fun displayGameBoard() {
        gameViewModel.gameBoardState.observe(viewLifecycleOwner) {
            gridAdapter.submitList(it.cellMatrix.flatten())
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                gameViewModel.moveOutcome.collect { moveOutcome ->
                    when (moveOutcome) {
                        MoveOutcome.WON ->
                            findNavController().navigate(R.id.action_gameFragment_to_winningFragment)

                        MoveOutcome.LOST -> handleLoseGame()
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun handleResetGame() {
        if (gameViewModel.gameBoardState.value!!.result == MoveOutcome.LOST) {
            binding.btnReset.clearAnimation()
            binding.tvGameOver.visibility = View.INVISIBLE
            binding.rvGrid.foreground = null
        }
        gameViewModel.onReset()
    }

    private fun handleLoseGame() {
        binding.tvGameOver.visibility = View.VISIBLE
        binding.rvGrid.foreground =
            ContextCompat.getDrawable(requireContext(), R.color.GameOverColor)
        AnimationUtils.loadAnimation(requireContext(), R.anim.reset_button_animation).also {
            binding.btnReset.startAnimation(it)
        }

    }


}