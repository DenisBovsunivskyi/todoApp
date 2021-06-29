package com.example.todolist.fragment.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.data.models.Priority
import com.example.todolist.data.models.ToDoData
import com.example.todolist.data.viewmodel.ToDoViewModel
import com.example.todolist.fragment.SharedViewModel
import com.example.todolist.hideKeyboard
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import org.threeten.bp.LocalDateTime
import java.time.format.DateTimeFormatter

class AddFragment : Fragment() {

    private val mToDoViewModel:ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add, container, false)
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        view.choose_priority.onItemSelectedListener = mSharedViewModel.listener
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add){
            insertDataToDB()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDB() {
        val mTitle = title_edit_text.text.toString()
        val mPriority = choose_priority.selectedItem.toString()
        val mDescription = description_edit_text.text.toString()
        val mTimeStamp = System.currentTimeMillis()
        val validation = mSharedViewModel.verifyDataFromUser(mTitle,mDescription)
        if(validation){
            //Insert data to database
            val newData = ToDoData(
                0,
                mTitle,
                mSharedViewModel.parsePriority(mPriority),
                mDescription,
                mTimeStamp

            )
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Successful add", Toast.LENGTH_SHORT).show()
            //Navigate Back
            activity?.let { hideKeyboard(it) }
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Fields are empty", Toast.LENGTH_SHORT).show()
        }
    }

}