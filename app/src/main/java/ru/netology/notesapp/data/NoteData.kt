package ru.netology.notesapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.notesapp.models.Note
import ru.netology.notesapp.utilities.DATABASE_NAME

@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
abstract class NoteData: RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object{

        @Volatile
        private var INSTANCE : NoteData? = null

        fun getData(context: Context) : NoteData{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteData::class.java,
                    DATABASE_NAME
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}