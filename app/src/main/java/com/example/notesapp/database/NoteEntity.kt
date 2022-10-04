package com.example.notesapp.database


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Notes_Table")
data class NoteEntity(

    val noteTitle: String,
    val noteLocation : String,
    val noteAudio: String,
    val noteImage : String,
    val noteDescription : String


){
    @PrimaryKey(autoGenerate = true) var id = 0
}
