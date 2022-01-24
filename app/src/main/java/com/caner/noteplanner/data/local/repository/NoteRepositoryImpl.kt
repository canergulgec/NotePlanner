package com.caner.noteplanner.data.local.repository

import com.caner.noteplanner.data.model.Note
import com.caner.noteplanner.data.local.NoteDao
import com.caner.noteplanner.utils.qualifier.IODispatcher
import com.caner.noteplanner.presentation.utils.NoteOrder
import com.caner.noteplanner.presentation.utils.OrderType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val taskDao: NoteDao,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : NoteRepository {

    override fun getAllNotes(noteOrder: NoteOrder): Flow<List<Note>> {
        return taskDao.getAllNotes().map { notes ->
            when (noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.createdAt }
                    }
                }

                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.createdAt }
                    }
                }
            }
        }.flowOn(dispatcher)
    }

    override suspend fun insert(note: Note) = taskDao.insertNote(note)

    override suspend fun update(note: Note) = taskDao.updateNote(note)

    override fun find(id: Int) = taskDao.findByID(id).flowOn(dispatcher)

    override suspend fun delete(id: Int) = taskDao.deleteNoteByID(id)
}