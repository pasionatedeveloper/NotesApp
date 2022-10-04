package com.example.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface NotesDAO {

    @Insert
    fun insertNewNote(newNote: NoteEntity)

    @Query("Select * from Notes_Table order by id ASC")
    fun getAllNotes() : LiveData<List<NoteEntity>>

    @Update
    fun updateNote(noteEntity: NoteEntity)

    @Delete
    fun deleteNotes(noteEntity: NoteEntity)

}