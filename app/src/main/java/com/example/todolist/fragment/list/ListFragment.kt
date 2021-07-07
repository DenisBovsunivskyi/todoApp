package com.example.todolist.fragment.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.viewmodel.ToDoViewModel
import com.example.todolist.fragment.SharedViewModel
import com.example.todolist.fragment.list.adapter.ListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlin.system.exitProcess


class ListFragment : Fragment() {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView = view.recycler_view
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        // Swipe to delete
        swipeToDelete(recyclerView)
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data->
            mSharedViewModel.checkIfDataBaseEmpty(data)
            adapter.setData(data)

        })
        mSharedViewModel.checkIfInternetContected(viewLifecycleOwner)
        mSharedViewModel.isInternetConnected.observe(viewLifecycleOwner, Observer {
            showNoInternetSnackBar(it)
        })
        mSharedViewModel.emptyDataBase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseViews(it)
        })
        view.fab.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        //Меню
        setHasOptionsMenu(true)
    return view


    }

    private fun showNoInternetSnackBar(it: Boolean) {
         if(!it) {
             getActivity()?.let {
                 Snackbar.make(
                     it.findViewById(android.R.id.content),
                     "No Internet", Snackbar.LENGTH_LONG
                 ).show()
             };
         }else{
             Toast.makeText(requireContext(),"Internet OK!", Toast.LENGTH_SHORT).show()

         }
    }


    private fun showEmptyDatabaseViews(emptyDatabase:Boolean) {
        if(emptyDatabase){
            view?.no_data_image_view?.visibility = View.VISIBLE
            view?.no_data_text_view?.visibility = View.VISIBLE
        }else {
            view?.no_data_image_view?.visibility = View.INVISIBLE
            view?.no_data_text_view?.visibility = View.INVISIBLE
        }

    }
    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallBack = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                mToDoViewModel.deleteItem(itemToDelete)
                Toast.makeText(requireContext(),"Successfully deleted!", Toast.LENGTH_SHORT).show()

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete_all -> confirmRemoveAll()
            R.id.exit_menu_button -> exitFromApp()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exitFromApp() {
        exitProcess(-1)
    }
    //show alert dialog to remove all notes
    private fun confirmRemoveAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(requireContext(),"Successfully deleted all!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){ _, _ -> }
        builder.setTitle("Delete all notes?")
        builder.setMessage("Are you sure you want to delete all notes?")
        builder.create().show()
    }
}
