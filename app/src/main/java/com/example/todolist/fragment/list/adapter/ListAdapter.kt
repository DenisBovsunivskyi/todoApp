package com.example.todolist.fragment.list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.todolist.R
import com.example.todolist.data.models.Priority
import com.example.todolist.data.models.ToDoData
import kotlinx.android.synthetic.main.row_layout.view.*

import androidx.navigation.findNavController
import com.example.todolist.fragment.list.ListFragmentDirections
import com.example.todolist.getFormattedTime
import java.util.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    var dataList = emptyList<ToDoData>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        )
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.title_text.text = dataList[position].title
        holder.itemView.description_text.text = dataList[position].description
        holder.itemView.row_background.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
            holder.itemView.findNavController().navigate(action)
        }
        val priority = dataList[position].priority

        when (priority) {
            Priority.HIGH -> holder.itemView.p_indicator.setCardBackgroundColor(holder.itemView.p_indicator.context.getColor(R.color.red))
            Priority.MEDIUM -> holder.itemView.p_indicator.setCardBackgroundColor(holder.itemView.p_indicator.context.getColor(R.color.yellow))
            Priority.LOW -> holder.itemView.p_indicator.setCardBackgroundColor(holder.itemView.p_indicator.context.getColor(R.color.green))
        }
            holder.itemView.time_text.text = getFormattedTime(dataList[position].timeStamp)

    }
    fun setData(toDoData:List<ToDoData>){
        this.dataList = toDoData
        notifyDataSetChanged()
    }

}