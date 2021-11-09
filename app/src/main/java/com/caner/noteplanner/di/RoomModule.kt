package com.caner.noteplanner.di

import android.content.Context
import androidx.room.Room
import com.caner.noteplanner.domain.local.NoteDao
import com.caner.noteplanner.domain.local.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDao = database.getNoteDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): NoteDatabase =
        Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "note-db"
        ).fallbackToDestructiveMigration().build()
}
