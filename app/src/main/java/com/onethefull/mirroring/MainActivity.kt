package com.onethefull.mirroring

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

import com.onethefull.mirroring.databinding.ActivityMainBinding
import com.onethefull.screenmirroring.ScreenMirroring


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var id = item.itemId

        if (id == R.id.action_fav) {
            //startActivity(Intent("android.settings.CAST_SETTINGS"))
            ScreenMirroring.getInstance().start(this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        ScreenMirroring.getInstance().init(this)
    }

    override fun onStop() {
        ScreenMirroring.getInstance().stop(this)
        super.onStop()
    }
}