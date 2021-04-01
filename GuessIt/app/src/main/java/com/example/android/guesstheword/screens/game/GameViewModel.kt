package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class GameViewModel : ViewModel(){

    companion object{
        // This is when the game is over
        private const val DONE = 0L
        // This is the number of milliseconds in a second
        private const val ONE_SECOND = 1000L
        // This is the total time of the game
        private const val COUNTDOWN_TIME = 60000L
    }
    // Declare timer
    private val timer : CountDownTimer

    /** [private, mutable] The remaining time in milliseconds - Initialized as null **/
    private val _remainTime = MutableLiveData<Long>()
    /** [public, immutable] **/
    val remainTime : LiveData<Long>
        get() = _remainTime

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

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                _remainTime.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                _remainTime.value = DONE
                _eventGameFinish.value = true
            }
        }
        // start timer
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        // Cancel CountDownTimer to avoid memory leaks
        timer.cancel()
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
           resetList()
        }
        _word.value = wordList.removeAt(0)

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