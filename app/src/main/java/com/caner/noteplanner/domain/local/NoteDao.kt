package com.caner.noteplanner.domain.local

import androidx.room.*
import com.caner.noteplanner.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("DELETE FROM note where id =:id")
    suspend fun deleteNoteByID(id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM note where id=:id")
    fun findByID(id: Int): Flow<Note>
}