package com.caner.noteplanner.di

import android.content.Context

import com.caner.noteplanner.domain.local.datastore.ThemeManager
import com.caner.noteplanner.domain.local.datastore.ThemeManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePreferenceManager(@ApplicationContext context: Context): ThemeManager {
        return ThemeManagerImpl(context = context)
    }
}