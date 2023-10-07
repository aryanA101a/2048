package com.example.a2048.view.fragment

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
import com.example.a2048.view.listener.OnSwipeTouchListener
import com.example.a2048.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private lateinit var gridAdapter: GridAdapter
    private val gameViewModel: GameViewModel by activityViewModels()

//    override fun onPause() {
//        Log.i("TAG", "onPause: ")
//        gameViewModel.persistState()
//        super.onPause()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        Log.i("TAG", "onResume: ")
//        gameViewModel.restorePersistedState()
//        super.onCreate(savedInstanceState)
//    }

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

            val gameTouchListener = createGameTouchListener()
            rvGrid.setOnTouchListener(gameTouchListener)
            clFragmentGame.setOnTouchListener(gameTouchListener)

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
        gridAdapter.submitList(gameViewModel.gameState.value!!.boardState.cellMatrix.flatten())

        Log.i("TAG", "initGrid:${gameViewModel.gameState.value!!} ")
    }

    private fun displayGameBoard() {

        gameViewModel.gameState.observe(viewLifecycleOwner) {
            gridAdapter.submitList(it.boardState.cellMatrix.flatten())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                gameViewModel.moveOutcome.collect { moveOutcome ->

                    when (moveOutcome) {
                        MoveOutcome.WON -> findNavController().navigate(R.id.action_gameFragment_to_winningFragment)

                        MoveOutcome.LOST -> handleLoseGame()

                        else -> Unit
                    }

                }
            }
        }
    }

    private fun handleResetGame() {
        gameViewModel.onReset()
        if (gameViewModel.gameState.value!!.result == MoveOutcome.LOST) {
            binding.apply {
                btnUndo.visibility=View.INVISIBLE
                btnReset.backgroundTintList=ContextCompat.getColorStateList(requireContext(), R.color.Board)
                btnReset.clearAnimation()
                tvGameOver.visibility = View.INVISIBLE
                rvGrid.foreground = null
            }
        }

    }

    private fun handleLoseGame() {
        binding.apply {
            btnUndo.visibility=View.INVISIBLE
            tvGameOver.visibility = View.VISIBLE
            rvGrid.foreground =
                ContextCompat.getDrawable(requireContext(), R.color.GameOverColor)
            btnReset.backgroundTintList=ContextCompat.getColorStateList(requireContext(), R.color.GameOverResetBtnColor)
            AnimationUtils.loadAnimation(requireContext(), R.anim.reset_button_animation).let{
                btnReset.startAnimation(it)
            }
        }

    }

    private fun createGameTouchListener() = object :
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
    }


}