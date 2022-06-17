package com.exsample.catapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.exsample.catapp.fragment.CatFragmentViewModel
import com.exsample.catapp.databinding.ActivityMainBinding
import com.exsample.catapp.fragment.CatFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, CatFragment()).commit()
    }
}