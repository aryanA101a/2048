package com.example.a2048.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.a2048.R
import com.example.a2048.databinding.FragmentWinningBinding
import com.example.a2048.viewmodel.GameViewModel


class WinningFragment : Fragment() {
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentWinningBinding.inflate(inflater, container, false).apply {
            btnResetOnWinningDialog.setOnClickListener {
                gameViewModel.onReset()
                findNavController().popBackStack()
            }
            btnContinue.setOnClickListener {
                findNavController().popBackStack()
            }

            return root
        }
    }
}