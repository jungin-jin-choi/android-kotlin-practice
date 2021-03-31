package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.ViewModel

// Create the GameViewModel class, extending ViewModel
// Add init and override onCleared. Add log statements to both

class GameViewModel: ViewModel(){
    init {
        Log.i("GameViewModel", "GameViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
    }
}