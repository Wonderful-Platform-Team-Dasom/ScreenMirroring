package com.onethefull.screenmirroring

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.onethefull.screenmirroring.databinding.ActivityStartMirroringBinding

class StartMirroringActivity :AppCompatActivity() {

    private lateinit var binding : ActivityStartMirroringBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        binding = ActivityStartMirroringBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}