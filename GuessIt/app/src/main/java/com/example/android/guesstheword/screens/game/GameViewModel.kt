package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Create the GameViewModel class, extending ViewModel
// Add init and override onCleared. Add log statements to both

class GameViewModel: ViewModel(){
    /** [private, mutable] The current word - Initialized as null **/
    private val _word = MutableLiveData<String>()
    /** [public, immutable] **/
    val word : LiveData<String>
        get() = _word

    /** [private, mutable] The current score - Initialized as null **/
    private val _score = MutableLiveData<Int>()
    /** [public, immutable] **/
    val score : LiveData<Int>
        get() = _score

    /** The list of words - the front of the list is the next word to guess **/
    private lateinit var wordList: MutableList<String>

    /** flag that indicates whether game has finished **/
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish : LiveData<Boolean>
        get() = _eventGameFinish


    init {
        Log.i("GameViewModel", "GameViewModel created!")
        resetList()
        nextWord()
        _score.value = 0
        _eventGameFinish.value = false
    }

    override fun onCleared() {
        super.onCleared()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            _eventGameFinish.value = true
        } else {
            _word.value = wordList.removeAt(0)
        }
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }


}