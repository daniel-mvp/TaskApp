package com.example.taskapp.ui.inter

import com.example.taskapp.ui.fragments.models.TaskModel

interface OnItemClickHome {
    fun update(taskModel: TaskModel)

    fun delete(taskModel: TaskModel)
}
