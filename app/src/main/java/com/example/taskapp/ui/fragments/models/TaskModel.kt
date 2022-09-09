package com.example.taskapp.ui.fragments.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "table_name")
data class TaskModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val task: String,
    val date: String,
    val regular: String
): Serializable
