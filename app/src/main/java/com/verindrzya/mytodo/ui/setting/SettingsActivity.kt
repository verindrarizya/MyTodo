package com.verindrzya.mytodo.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.verindrzya.mytodo.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private val binding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

}