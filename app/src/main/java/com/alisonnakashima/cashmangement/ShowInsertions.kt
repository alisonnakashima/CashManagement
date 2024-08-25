package com.alisonnakashima.cashmangement

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.alisonnakashima.cashmangement.Adapter.ListAdapter
import com.alisonnakashima.cashmangement.database.DatabaseHandler
import com.alisonnakashima.cashmangement.databinding.ActivityShowInsertionsBinding

class ShowInsertions : AppCompatActivity() {

    private lateinit var binding: ActivityShowInsertionsBinding
    private lateinit var banco: DatabaseHandler
    private lateinit var lvInsertions: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowInsertionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lvInsertions = findViewById(R.id.lvInsertions)

        val registros = banco.cursorList()


        val adapter = ListAdapter(this, registros)

        lvInsertions.adapter = adapter


    }
}