package com.example.todolist

import android.app.Activity
import android.content.Context
import android.text.format.DateUtils
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

fun getFormattedTime(time: Long):String{
    val timeStamp = Date(time)
    val formatterTrue = SimpleDateFormat("HH:mm")
    val formatterFalse = SimpleDateFormat("dd-MM-yyyy")
    if(DateUtils.isToday(time)){
        return formatterTrue.format(time)
    }else{
        return formatterFalse.format(time)
    }
}
fun hideKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    // Check if no view has focus
    val currentFocusedView = activity.currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

