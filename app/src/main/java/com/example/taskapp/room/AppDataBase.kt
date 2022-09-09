package com.example.taskapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskapp.ui.fragments.models.TaskModel

@Database(entities = [TaskModel::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}