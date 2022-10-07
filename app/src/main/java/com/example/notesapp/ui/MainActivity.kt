package com.example.notesapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.NotesViewModel
import com.example.notesapp.adapter.RecyclerAdapter
import com.example.notesapp.database.NoteEntity
import com.example.notesapp.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener


class MainActivity : AppCompatActivity(), RecyclerAdapter.NoteClickDeleteInterface {

    lateinit var adapter: RecyclerAdapter
    lateinit var notesList: ArrayList<NoteEntity>
    lateinit var notesViewModal: NotesViewModel

    lateinit var adView: AdView
    lateinit var adRequest: AdRequest
    var searchList: MutableList<NoteEntity> = mutableListOf()


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        notesList = ArrayList<NoteEntity>()



        MobileAds.initialize(this, object : OnInitializationCompleteListener {
            override fun onInitializationComplete(initializationStatus: InitializationStatus) {

            }
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val newList = searchList.filter {
                    it.noteTitle.contains(p0!!)
                }
                adapter.allNotesList = newList
                adapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(msg: String): Boolean {
                if (msg.isNullOrEmpty()) {
                    adapter.allNotesList = searchList
                    adapter.notifyDataSetChanged()
                }
                return true
            }
        })



        adView = binding.adView
        adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


        val layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerViewNotes.layoutManager = layoutManager
        binding.recyclerViewNotes.setHasFixedSize(true)
        adapter = RecyclerAdapter(applicationContext, listOf(), this);
        binding.recyclerViewNotes.adapter = adapter
        notesViewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NotesViewModel::class.java)
        notesViewModal.readAll.observe(this) {
            adapter.allNotesList = it
            searchList.addAll(it)
            adapter.notifyDataSetChanged()
        }

        binding.fab.setOnClickListener {
            val intent = Intent(applicationContext, AddNewNoteActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }


    override fun onDeleteIconClick(note: NoteEntity) {

        notesViewModal.deleteNote(note)
        Toast.makeText(this, "${note.noteTitle} Deleted", Toast.LENGTH_LONG).show()
    }


}