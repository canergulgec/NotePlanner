package com.caner.noteplanner.di

import com.caner.noteplanner.domain.repository.NoteRepository
import com.caner.noteplanner.domain.repository.NoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideNoteRepositoryImp(repositoryImp: NoteRepositoryImpl): NoteRepository

}
