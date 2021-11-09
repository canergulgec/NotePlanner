package com.caner.noteplanner.domain.repository

import com.caner.noteplanner.data.model.Note
import com.caner.noteplanner.domain.utils.NoteOrder
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllNotes(noteOrder: NoteOrder): Flow<List<Note>>

    suspend fun insert(note: Note)

    suspend fun update(note: Note)

    fun find(id: Int): Flow<Note>

    suspend fun delete(id: Int)
}