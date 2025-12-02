package com.example.donshy.data

import android.content.Context
import com.example.donshy.data.repository.AudioRepository
import com.example.donshy.data.repository.AudioRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  RepositoryModule {
    @Provides
    @Singleton
    fun bindAudioRepository(@ApplicationContext context: Context): AudioRepository =
        AudioRepositoryImpl(context)
}