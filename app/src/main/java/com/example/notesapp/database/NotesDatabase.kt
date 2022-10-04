package com.example.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [NoteEntity::class],
    version = 1
)
abstract class NotesDatabase : RoomDatabase() {


    abstract fun noteDAO() :  NotesDAO


    companion object{

        @Volatile
        private var INSTANCE : NotesDatabase? = null

        fun getDatabase(context: Context): NotesDatabase{


            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "notes_database"
                ).allowMainThreadQueries().build()
                INSTANCE=instance
                return instance
            }
        }
    }
}