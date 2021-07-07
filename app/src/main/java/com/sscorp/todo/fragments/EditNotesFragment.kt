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
import com.sscorp.todo.R
import com.sscorp.todo.models.Importance
import com.sscorp.todo.models.Note
import com.sscorp.todo.presenter.EditNotePresenter
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

class EditNotesFragment : Fragment() {

    companion object {
        private const val NOTE_KEY = "note_key"

        fun newInstance(note: Note): EditNotesFragment {
            val fragment = EditNotesFragment()
            val bundle = Bundle()
            bundle.putParcelable(NOTE_KEY, note)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var note: Note? = null

    private lateinit var presenter: EditNotePresenter

    private var chosenDate: Date? = null

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
        val view = inflater.inflate(R.layout.fragment_edit_notes, container, false)
        note = arguments?.getParcelable(NOTE_KEY)
        init(view)
        if (note != null)
            initForEdit()

        return view
    }

    private fun initForEdit() {
        chosenDate = note!!.expirationDate
        when (note!!.importance) {
            Importance.NO -> spinner.setSelection(0)
            Importance.LOW -> spinner.setSelection(1)
            Importance.HIGH -> spinner.setSelection(2)
        }
        setDateToTextView()
        editTextDescription.text = SpannableStringBuilder(note!!.description)
        textViewDelete.setOnClickListener {
            presenter.deleteNote(note!!)
        }
    }

    private fun init(view: View) {
        initSpinner(view)
        initToolbar(view)

        dateTextView = view.findViewById(R.id.textViewChosenDate)
        editTextDescription = view.findViewById(R.id.editTextDescription)
        switch = view.findViewById(R.id.switch_date)
        if (note?.expirationDate != null)
            switch.isChecked = true
        textViewDelete = view.findViewById(R.id.textViewDelete)
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
        view.findViewById<TextView>(R.id.textViewSave).setOnClickListener {
            saveNote()
        }
        presenter = EditNotePresenter(this)
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
        if (note == null) {
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

    private fun saveNote() {
        val newNote = parseNote() ?: return
        if (note == null)
            presenter.addNote(newNote)
        else
            presenter.updateNote(note!!, newNote)
    }

    private fun parseNote(): Note? {
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
            0 -> Importance.NO
            1 -> Importance.LOW
            2 -> Importance.HIGH
            else -> throw IllegalArgumentException()
        }
        return Note(description, importance, chosenDate)
    }
}