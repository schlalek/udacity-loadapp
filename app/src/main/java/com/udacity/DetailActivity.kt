package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val name = intent.getStringExtra("name")
        val status = intent.getStringExtra("status")

        binding.content.statusTextView.text = status
        binding.content.fileNameTextView.text = name

        binding.content.okButton.setOnClickListener { backToMain() }
        
    }

    private fun backToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
