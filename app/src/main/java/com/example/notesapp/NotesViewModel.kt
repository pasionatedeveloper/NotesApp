package com.example.notesapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.notesapp.database.NoteEntity
import com.example.notesapp.database.NoteRepository
import com.example.notesapp.database.NotesDatabase

class NotesViewModel(application: Application) : AndroidViewModel(application) {


    private val repository : NoteRepository
    var readAll : LiveData<List<NoteEntity>>

    init {
        val dao = NotesDatabase.getDatabase(application).noteDAO()
        repository = NoteRepository(dao)
        readAll = repository.getAllNotes()
    }


    fun addNote(note: NoteEntity){
        repository.insertNotes(note)
    }
    fun deleteNote (note: NoteEntity)  {
        repository.deleteNotes(note)
    }


}


