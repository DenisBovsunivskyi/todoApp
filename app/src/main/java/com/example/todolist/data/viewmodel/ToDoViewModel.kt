package com.example.todolist.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.ToDoDatabase
import com.example.todolist.data.models.ToDoData
import com.example.todolist.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application ):AndroidViewModel(application) {
    private val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
    private val repository: ToDoRepository = ToDoRepository(toDoDao)

     val getAllData: LiveData<List<ToDoData>> = repository.getAllData

    fun insertData(toDoData: ToDoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.insertData(toDoData)
        }
    }
    fun updateData(toDoData: ToDoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateData(toDoData)
        }
    }
    fun deleteItem(toDoData:ToDoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteItem(toDoData)
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAll()
        }
    }
}