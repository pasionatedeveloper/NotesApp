package com.example.notesapp.database

import androidx.lifecycle.LiveData




class NoteRepository(private val noteDAO : NotesDAO) {

    fun insertNotes(noteEntity: NoteEntity)=
       noteDAO.insertNewNote(noteEntity)

    fun updateNotes(noteEntity: NoteEntity)=
        noteDAO.updateNote(noteEntity)

    fun getAllNotes() : LiveData<List<NoteEntity>> =
        noteDAO.getAllNotes()

    fun deleteNotes(noteEntity: NoteEntity)=
        noteDAO.deleteNotes(noteEntity)

}