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
    // create instance of Data class
    private val myName = MyName("JungIn Choi")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Should also create the binding object to connect the main activity view and layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        // Should connect the data class instance to the layout
        binding.myName = myName

        // Replace findViewById with data binding
        binding.doneButton.setOnClickListener{
            addNickname(it)
        }
    }

    private fun addNickname(view: View) {
        // improve readability by using kotlin scope function
        binding.apply{
            myName?.nickname = nicknameEdit.text.toString()
            // invalidate all binding expressions in order to refresh the UI with new data
            invalidateAll()
            nicknameEdit.visibility = View.GONE
            doneButton.visibility = View.GONE
            nicknameText.visibility = View.VISIBLE
        }

        // hide keyboards
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}