package com.vovan.diplomaapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Configuration
import androidx.work.WorkManager
import com.vovan.diplomaapp.R
import com.vovan.diplomaapp.data.sharedPreference.SharedPreferenceDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CommonModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.sp_file),
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideSharedPreferenceDataSource(sharedPreferences: SharedPreferences) =
        SharedPreferenceDataSource(sharedPreferences)


    @Provides
    fun providesWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)
}