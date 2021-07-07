package com.sscorp.todo.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.sscorp.todo.R
import com.sscorp.todo.activities.MainActivity
import com.sscorp.todo.models.Importance
import com.sscorp.todo.models.Note
import java.text.SimpleDateFormat
import java.util.*

class NotesViewHolder(
    private val context: Context,
    private val onCheckBoxClick: (note: Note, pos: Int) -> Unit,
    view: View
) : RecyclerView.ViewHolder(view) {

    private val description = view.findViewById<TextView>(R.id.textViewDescription)
    private val checkBox = view.findViewById<AppCompatCheckBox>(R.id.checkBoxIsDone)
    private val iconPriority = view.findViewById<ImageView>(R.id.imageViewPriority)
    private val textViewDate = view.findViewById<TextView>(R.id.textViewDate)
    private val iconInfo = view.findViewById<ImageView>(R.id.imageViewInfo)

    fun bind(note: Note) {
        description.text = note.description
        checkBox.isChecked = note.isDone

        setCheckBoxStyle(note)
        setIcons(note)
        setDescriptionStyle(note)
        setDate(note)

        checkBox.setOnClickListener {
            onCheckBoxClick(note, absoluteAdapterPosition)
        }
        iconInfo.setOnClickListener {
            (context as MainActivity).openEditNoteFragment(note)
        }
    }

    private fun setCheckBoxStyle(note: Note) {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked)
        )

        if (note.expirationDate != null &&
            note.expirationDate < Calendar.getInstance().time
        ) {
            checkBox.buttonTintList = ColorStateList(
                states,
                intArrayOf(
                    ResourcesCompat.getColor(context.resources, R.color.green, null),
                    ResourcesCompat.getColor(context.resources, R.color.red, null)
                )
            )
        } else {
            checkBox.buttonTintList = ColorStateList(
                states,
                intArrayOf(
                    ResourcesCompat.getColor(context.resources, R.color.green, null),
                    ResourcesCompat.getColor(context.resources, R.color.separator_support, null)
                )
            )
        }
    }

    private fun setIcons(note: Note) {
        if (note.importance == Importance.NO) {
            iconPriority.visibility = View.GONE
            return
        }
        iconPriority.visibility = View.VISIBLE
        if (note.importance == Importance.LOW)
            iconPriority.setImageResource(R.drawable.ic_low_priority)
        else
            iconPriority.setImageResource(R.drawable.ic_priority_high)
    }

    private fun setDescriptionStyle(note: Note) {
        if (note.isDone)
            description.paintFlags =
                description.paintFlags.or(Paint.STRIKE_THRU_TEXT_FLAG)
        else
            description.paintFlags =
                description.paintFlags.and(Paint.STRIKE_THRU_TEXT_FLAG.inv())
    }

    private fun setDate(note: Note) {
        if (note.expirationDate == null) {
            textViewDate.visibility = View.GONE
            return
        }
        val simpleDate = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        textViewDate.visibility = View.VISIBLE
        textViewDate.text = simpleDate.format(note.expirationDate)
    }
}