package com.sscorp.todo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sscorp.todo.App
import com.sscorp.todo.R
import com.sscorp.todo.activities.MainActivity
import com.sscorp.todo.models.Task
import com.sscorp.todo.tasks_list.SwipeCallback
import com.sscorp.todo.tasks_list.TasksAdapter
import com.sscorp.todo.view_models.TasksViewModel
import javax.inject.Inject

class TasksFragment : Fragment() {

    @Inject
    lateinit var tasksViewModel: TasksViewModel

    private lateinit var tasksAdapter: TasksAdapter

    private lateinit var textViewCountDone: TextView
    private lateinit var visibilityIcon: ImageView
    private lateinit var notesList: RecyclerView
    private lateinit var addNoteButton: FloatingActionButton

    private var currentVisibility = Visibility.NORMAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (App.getApplicationContext() as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        textViewCountDone = view.findViewById(R.id.text_view_count)
        visibilityIcon = view.findViewById(R.id.image_view_visibility)
        notesList = view.findViewById(R.id.list_tasks)
        addNoteButton = view.findViewById(R.id.btn_add_note)
        configListOfNotes()
        (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.toolbar))
        visibilityIcon.setOnClickListener {
            changeVisibility()
        }
        view.findViewById<Toolbar>(R.id.toolbar)
            .setOnClickListener {
                notesList.smoothScrollToPosition(0)
            }
        addNoteButton.setOnClickListener {
            (context as MainActivity).openEditNoteFragment()
        }
    }

    private fun changeVisibility() {
        currentVisibility = if (currentVisibility == Visibility.NORMAL) {
            visibilityIcon.setImageResource(R.drawable.ic_visibility_off)
            tasksViewModel.loadAllTasksFromDb()
            Visibility.ALL
        } else {
            visibilityIcon.setImageResource(R.drawable.ic_visibility)
            tasksViewModel.loadDoneTasksFromDb()
            Visibility.NORMAL
        }
    }

    private fun configListOfNotes() {
        tasksAdapter = TasksAdapter(requireContext(), this, ::onCheckBoxClicked)
        val itemTouchHelper = ItemTouchHelper(SwipeCallback(tasksAdapter))
        itemTouchHelper.attachToRecyclerView(notesList)
        notesList.adapter = tasksAdapter
    }

    private fun updateCountOfCompleted(count: Int) {
        textViewCountDone.text = String.format(getString(R.string.count_done), count)
    }

    private fun onCheckBoxClicked(task: Task, position: Int) {
        tasksViewModel.setTaskDone(task)
        updateCountOfCompleted(tasksViewModel.tasks.value!!.filter { it.done }.size)
        updateListItemView(position)
    }

    fun deleteNote(task: Task) {
        tasksViewModel.deleteTask(task)
        updateCountOfCompleted(tasksViewModel.tasks.value!!.filter { it.done }.size)
    }

    private fun updateListItemView(position: Int) {
        tasksAdapter.notifyItemChanged(position)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tasksViewModel.tasks.observe(viewLifecycleOwner) {
            onTasksDataChanged(it)
        }
    }

    fun checkNote(task: Task, position: Int) {
        tasksViewModel.taskCheck(task)
        updateCountOfCompleted(tasksViewModel.tasks.value!!.filter { it.done }.size)
        updateListItemView(position)
    }

    private fun onTasksDataChanged(tasks: List<Task>) {
        return if (currentVisibility == Visibility.ALL)
            tasksAdapter.submitList(tasks.sortedDescending())
        else
            tasksAdapter.submitList(tasks.filter { !it.done }.sortedDescending())
    }

    enum class Visibility {
        NORMAL, ALL
    }

    override fun onStart() {
        super.onStart()
        if (tasksViewModel.tasks.value != null) {
            onTasksDataChanged(tasksViewModel.tasks.value!!)
            updateCountOfCompleted(tasksViewModel.tasks.value!!.filter { it.done }.size)
        }
    }
}