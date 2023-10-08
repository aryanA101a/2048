package com.aryan.a2048

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.aryan.a2048.R
import com.aryan.a2048.databinding.ActivityMainBinding
import com.example.a2048.SavedGameProto
import com.aryan.a2048.data.model.SavedGameSerializer
import com.aryan.a2048.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint

val Context.dataStore: DataStore<SavedGameProto> by dataStore(
    fileName = "GameStateStore",
    serializer = SavedGameSerializer
)

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val gameViewModel: GameViewModel by viewModels()

    override fun onPause() {
        Log.i("TAG", "onPause: ")
        gameViewModel.persistState()
        super.onPause()
    }

    override fun onResume() {
        Log.i("TAG", "onResume: ")
        gameViewModel.restorePersistedState()
        super.onResume()
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_main)

    }

}