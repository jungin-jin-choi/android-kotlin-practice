package com.example.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.aboutme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Late initialization for binding object(non-nullable)
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Should also create the binding object to connect the activity and layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Replace findViewById with data binding
        binding.doneButton.setOnClickListener{
            addNickname(it)
        }
    }

    private fun addNickname(view: View) {
        // set nickname text
        binding.nicknameText.text = binding.nicknameEdit.text
        // hide EditText and Button, show TextView
        binding.nicknameEdit.visibility = View.GONE
        binding.doneButton.visibility = View.GONE
        binding.nicknameText.visibility = View.VISIBLE

        // hide keyboards
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}