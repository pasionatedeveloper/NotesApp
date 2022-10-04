package com.example.notesapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.NotesViewModel
import com.example.notesapp.adapter.RecyclerAdapter
import com.example.notesapp.database.NoteEntity
import com.example.notesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var adapter: RecyclerAdapter
    lateinit var notesList: ArrayList<NoteEntity>
    lateinit var notesViewModal: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerViewNotes.layoutManager = layoutManager
        binding.recyclerViewNotes.setHasFixedSize(true)
        adapter = RecyclerAdapter(applicationContext,listOf());
        binding.recyclerViewNotes.adapter = adapter
        notesViewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NotesViewModel::class.java)
        notesViewModal.readAll.observe(this) {
            adapter.allNotesList = it
            adapter.notifyDataSetChanged()
        }

        binding.fab.setOnClickListener {
            val intent = Intent(applicationContext, AddNewNoteActivity::class.java)
            startActivity(intent)
        }
    }


}