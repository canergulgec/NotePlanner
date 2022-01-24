package com.caner.noteplanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.caner.noteplanner.data.model.Note

@Database(entities = [Note::class], version = 2, exportSchema = true)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
}
