package com.onethefull.mirroring

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseAppIndex
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.builders.Actions
import com.google.firebase.appindexing.Indexable;
import com.onethefull.mirroring.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
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

        if(id==R.id.action_fav){
            startActivity(Intent("android.settings.CAST_SETTINGS"))
        }
        return super.onOptionsItemSelected(item)
    }

    // After
    fun getAction(): Action{
        return Actions.newView("Main Page", Uri.parse("http://").toString())
    }
    override fun onStart() {
        super.onStart()
       // FirebaseAppIndex.getInstance(this).update(getIndexable());
        FirebaseUserActions.getInstance(this).start(getAction());
    }

    override fun onStop() {
        FirebaseUserActions.getInstance(this).end(getAction());

        super.onStop()
    }
}