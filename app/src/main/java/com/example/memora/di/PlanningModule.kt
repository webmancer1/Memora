package com.example.memora.di

import android.content.Context
import androidx.room.Room
import com.example.memora.data.local.PlanningDatabase
import com.example.memora.data.local.dao.PlanningDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlanningModule {

    @Provides
    @Singleton
    fun providePlanningDatabase(@ApplicationContext context: Context): PlanningDatabase {
        return Room.databaseBuilder(
            context,
            PlanningDatabase::class.java,
            "planning_database"
        )
        // Note: For production app, add migrations here
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun providePlanningDao(database: PlanningDatabase): PlanningDao {
        return database.planningDao()
    }
}
