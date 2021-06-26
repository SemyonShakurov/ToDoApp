package com.sscorp.todo.adapters

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.sscorp.todo.R
import com.sscorp.todo.activities.MainActivity
import com.sscorp.todo.models.Importance
import com.sscorp.todo.models.Note

class NotesViewHolder(private val context: Context, view: View) : RecyclerView.ViewHolder(view) {

    private val description = view.findViewById<TextView>(R.id.textViewDescription)
    private val checkBox = view.findViewById<CheckBox>(R.id.checkBoxIsDone)
    private val iconVisibility = view.findViewById<ImageView>(R.id.imageViewPriority)

    fun bind(note: Note) {
        description.text = note.description
        checkBox.isChecked = note.isDone

        when {
            note.isDone -> setDoneState()
            note.importance == Importance.NO -> setNormalState()
            note.importance == Importance.LOW -> setLowState()
            note.importance == Importance.HIGH -> setHighState()
        }

        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                note.isDone = true
                setDoneState()
            } else {
                note.isDone = false
                when (note.importance) {
                    Importance.NO -> setNormalState()
                    Importance.LOW -> setLowState()
                    Importance.HIGH -> setHighState()
                }
            }
            (context as? MainActivity)?.updateCompletedTasks()
        }
    }

    private fun setDoneState() {
        description.paintFlags =
            description.paintFlags.or(Paint.STRIKE_THRU_TEXT_FLAG)
        checkBox.buttonDrawable =
            ResourcesCompat.getDrawable(context.resources, R.drawable.ic_checked, null)
        iconVisibility.visibility = View.GONE
    }

    private fun setNormalState() {
        description.paintFlags =
            description.paintFlags.and(Paint.STRIKE_THRU_TEXT_FLAG.inv())
        checkBox.buttonDrawable =
            ResourcesCompat.getDrawable(context.resources, R.drawable.ic_unchecked, null)
        iconVisibility.visibility = View.GONE
    }

    private fun setLowState() {
        description.paintFlags =
            description.paintFlags.and(Paint.STRIKE_THRU_TEXT_FLAG.inv())
        checkBox.buttonDrawable =
            ResourcesCompat.getDrawable(context.resources, R.drawable.ic_unchecked, null)
        iconVisibility.setImageResource(R.drawable.ic_low_priority)
        iconVisibility.visibility = View.VISIBLE
    }

    private fun setHighState() {
        description.paintFlags =
            description.paintFlags.and(Paint.STRIKE_THRU_TEXT_FLAG.inv())
        checkBox.buttonDrawable =
            ResourcesCompat.getDrawable(context.resources, R.drawable.ic_unchecked, null)
        iconVisibility.setImageResource(R.drawable.ic_priority_high)
        iconVisibility.visibility = View.VISIBLE
    }
}