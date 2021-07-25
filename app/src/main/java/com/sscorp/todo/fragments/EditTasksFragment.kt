package com.sscorp.todo.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.sscorp.todo.App
import com.sscorp.todo.R
import com.sscorp.todo.models.Priority
import com.sscorp.todo.models.Task
import com.sscorp.todo.view_models.TasksViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EditTasksFragment : Fragment() {

    @Inject
    lateinit var tasksViewModel: TasksViewModel

    private var task: Task? = null

    private var chosenDate: Date? = null

    private val args: EditTasksFragmentArgs by navArgs()

    private lateinit var dateTextView: TextView
    private lateinit var editTextDescription: EditText
    private lateinit var spinner: Spinner
    private lateinit var switch: SwitchCompat
    private lateinit var textViewDelete: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_tasks, container, false)
        task = args.task
        init(view)
        if (task != null)
            initForEdit()
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (App.getApplicationContext() as App).appComponent.inject(this)
    }

    private fun init(view: View) {
        initSpinner(view)
        initToolbar(view)

        dateTextView = view.findViewById(R.id.text_view_chosen_date)
        editTextDescription = view.findViewById(R.id.edit_text_description)
        switch = view.findViewById(R.id.switch_date)
        if (task?.deadline != null)
            switch.isChecked = true
        textViewDelete = view.findViewById(R.id.text_view_delete)
        setTextViewDeleteStyle()
        view.findViewById<ImageView>(R.id.icon_close).setOnClickListener {
            requireActivity().onBackPressed()
        }
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                addDate()
            else
                removeDate()
        }
        view.findViewById<TextView>(R.id.text_view_save).setOnClickListener {
            saveTask()
        }
    }

    private fun initForEdit() {
        chosenDate = task!!.deadline
        when (task!!.priority) {
            Priority.BASIC -> spinner.setSelection(0)
            Priority.LOW -> spinner.setSelection(1)
            Priority.IMPORTANT -> spinner.setSelection(2)
        }
        setDateToTextView()
        editTextDescription.text = SpannableStringBuilder(task!!.text)
        textViewDelete.setOnClickListener {
            tasksViewModel.deleteTask(task!!)
            activity?.onBackPressed()
        }
    }

    private fun setDateToTextView() {
        if (chosenDate != null) {
            val blue = ContextCompat.getColor(requireContext(), R.color.blue)
            dateTextView.setTextColor(blue)
            val simpleDate = SimpleDateFormat("dd.MM.yyyy", Locale.US)
            dateTextView.text = simpleDate.format(chosenDate!!)
        } else {
            val grey = ContextCompat.getColor(requireContext(), R.color.tertiary_label)
            dateTextView.setTextColor(grey)
            dateTextView.text = resources.getString(R.string.no)
        }
    }

    private fun initSpinner(view: View) {
        spinner = view.findViewById(R.id.spinner_priority)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.priority_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item)
            spinner.adapter = adapter
        }
    }

    private fun initToolbar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar_edit_note)
        val mainActivity = activity as AppCompatActivity
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    private fun setTextViewDeleteStyle() {
        val color: Int
        val icon: Int
        if (task == null) {
            color = ContextCompat.getColor(requireContext(), R.color.label_disable)
            icon = R.drawable.ic_delete_grey
        } else {
            color = ContextCompat.getColor(requireContext(), R.color.red)
            icon = R.drawable.ic_delete_red
        }
        textViewDelete.setTextColor(color)
        textViewDelete.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
    }

    private fun addDate() {
        val listener = DatePickerDialog
            .OnDateSetListener { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                chosenDate = calendar.time
                setDateToTextView()
            }
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
        val blue = ContextCompat.getColor(requireContext(), R.color.blue)
        datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(blue)
        datePicker.getButton(DatePickerDialog.BUTTON_NEUTRAL).setTextColor(blue)
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(blue)
    }

    private fun removeDate() {
        chosenDate = null
        setDateToTextView()
    }

    private fun saveTask() {
        val newTask = parseTask() ?: return
        if (task == null)
            tasksViewModel.addTask(newTask)
        else
            tasksViewModel.updateTask(task!!, newTask)
        activity?.onBackPressed()
    }

    private fun parseTask(): Task? {
        val description = editTextDescription.text.toString()
        if (description == "") {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.description_empty),
                Toast.LENGTH_SHORT
            ).show()
            return null
        }
        val importance = when (spinner.selectedItemPosition) {
            0 -> Priority.BASIC
            1 -> Priority.LOW
            2 -> Priority.IMPORTANT
            else -> throw IllegalArgumentException()
        }
        return Task(text = description, priority = importance, deadline = chosenDate, done = false)
    }
}