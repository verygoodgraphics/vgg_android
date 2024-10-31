package com.example.verygoodgraphics.android.demo.activity

import android.app.Activity
import android.os.Bundle
import com.example.verygoodgraphics.android.demo.databinding.ActivityVggXmlDemoBinding

class VggXmlDemoActivity : Activity() {
    private lateinit var binding: ActivityVggXmlDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVggXmlDemoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}