package com.example.todolist.fragment

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.todolist.ConnectionLiveData
import com.example.todolist.R
import com.example.todolist.data.models.Priority
import com.example.todolist.data.models.ToDoData


class SharedViewModel(application: Application): AndroidViewModel(application) {
    val mApplication = application
    val emptyDataBase: MutableLiveData<Boolean> = MutableLiveData(true)
    val isInternetConnected: MutableLiveData<Boolean> = MutableLiveData(true)
    lateinit var connectionLiveData:ConnectionLiveData

    fun checkIfDataBaseEmpty(toDoData: List<ToDoData>){
        emptyDataBase.value = toDoData.isEmpty()
    }
    fun checkIfInternetContected(viewLifecycleOwner: LifecycleOwner){
        connectionLiveData = ConnectionLiveData(mApplication)
        connectionLiveData.observe(viewLifecycleOwner, Observer{isNetvorkAwailable ->
            isInternetConnected.value = isNetvorkAwailable
        })
    }

    val listener: AdapterView.OnItemSelectedListener = object :
    AdapterView.OnItemSelectedListener{
        override fun onItemSelected(
            parent: AdapterView<*>?, view: View?,
            position: Int,
            id: Long) {
            when(position) {
                0 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red))}
                1 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow))}
                2 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green))}
            }

        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}

    }
    fun isDataSame(title: String,description: String,newTitle:String,newDescription:String): Boolean {
        return title == newTitle && description == newDescription
    }

     fun verifyDataFromUser(title: String, description: String):Boolean {
        return if(TextUtils.isEmpty(title) || TextUtils.isEmpty(description)){
            false
        }else !(title.isEmpty() || description.isEmpty())
    }
    fun parsePriority(priority: String): Priority {
        return when(priority){
            "High Priority" -> {
                Priority.HIGH}
            "Medium Priority" -> {
                Priority.MEDIUM}
            "Low Priority" -> {
                Priority.LOW}
            else -> Priority.LOW
        }
    }
     fun parsePriorityToInt(priority: Priority):Int{
        return when(priority){
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }
}