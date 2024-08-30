package com.alisonnakashima.cashmangement

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.alisonnakashima.cashmangement.Adapter.ListAdapter
import com.alisonnakashima.cashmangement.database.DatabaseHandler
import com.alisonnakashima.cashmangement.databinding.ActivityShowInsertionsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShowInsertions : AppCompatActivity() {

    private lateinit var binding: ActivityShowInsertionsBinding
    private lateinit var banco: DatabaseHandler
    private lateinit var lvInsertions: ListView
    private lateinit var fbFloatingButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowInsertionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler(this)
        lvInsertions = findViewById(R.id.lvInsertions)
        fbFloatingButton = findViewById(R.id.fbFloatingButton)

        val registros = banco.cursorList()
        val adapter = ListAdapter(this, registros)

        lvInsertions.adapter = adapter

        fbFloatingButton.setOnClickListener{
            val intent = Intent(this, GraphScreen::class.java)
            startActivity( intent )
        }

    }
}