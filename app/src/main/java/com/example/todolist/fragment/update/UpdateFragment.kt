package com.example.todolist.fragment.update

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.R
import com.example.todolist.data.models.ToDoData
import com.example.todolist.data.viewmodel.ToDoViewModel
import com.example.todolist.fragment.SharedViewModel
import com.example.todolist.getFormattedTime
import com.example.todolist.hideKeyboard
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*


class UpdateFragment : Fragment() {
    private val args by navArgs<UpdateFragmentArgs>()

    private val mSharedViewModel:SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        setHasOptionsMenu(true)

        view.title_update_text.setText(args.currentItem.title)
        view.description_update_text.setText(args.currentItem.description)
        view.choose_priority_update.setSelection(mSharedViewModel.parsePriorityToInt(args.currentItem.priority))
        view.choose_priority_update.onItemSelectedListener = mSharedViewModel.listener
        view.time_update_text.text = ("Last update:"+getFormattedTime(args.currentItem.timeStamp))

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_change_menu -> updateItem()
            R.id.delete_single_item_menu -> confirmItemRemove()
        }
        return super.onOptionsItemSelected(item)
    }



    private fun updateItem() {
        val title = title_update_text.text.toString()
        val description = description_update_text.text.toString()
        val getPriority = choose_priority_update.selectedItem.toString()
        val timeStamp:Long
        if(mSharedViewModel.isDataSame(args.currentItem.title,args.currentItem.description,title,description)){
            timeStamp = args.currentItem.timeStamp
        }else{
            timeStamp = System.currentTimeMillis()
        }

        val validation = mSharedViewModel.verifyDataFromUser(title,description)
        if(validation){
            val updateItem = ToDoData(
                args.currentItem.id,
                title,
                mSharedViewModel.parsePriority(getPriority),
                description,
                timeStamp
            )
            mToDoViewModel.updateData(updateItem)
            Toast.makeText(requireContext(),"Successfully updated!",Toast.LENGTH_SHORT).show()
            //Nav back
            activity?.let { hideKeyboard(it) }
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(),"Please set text in all fields!",Toast.LENGTH_SHORT).show()

        }
    }
    //Show dialog for remove item
    private fun confirmItemRemove() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
            mToDoViewModel.deleteItem(args.currentItem)
            Toast.makeText(requireContext(),"Successfully deleted!",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){ _, _ -> }
        builder.setTitle("Delete '${args.currentItem.title}'?")
        builder.setMessage("Are you sure you want to delete ${args.currentItem.title}?")
        builder.create().show()
    }

}