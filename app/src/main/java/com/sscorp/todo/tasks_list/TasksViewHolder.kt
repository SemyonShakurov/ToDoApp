package com.sscorp.todo.tasks_list

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
import com.sscorp.todo.models.Priority
import com.sscorp.todo.models.Task
import java.text.SimpleDateFormat
import java.util.*

class TasksViewHolder(
    private val context: Context,
    private val onCheckBoxClick: (task: Task, pos: Int) -> Unit,
    view: View
) : RecyclerView.ViewHolder(view) {

    private val description = view.findViewById<TextView>(R.id.text_view_description)
    private val checkBox = view.findViewById<AppCompatCheckBox>(R.id.check_box_is_done)
    private val iconPriority = view.findViewById<ImageView>(R.id.image_view_priority)
    private val textViewDate = view.findViewById<TextView>(R.id.text_view_date)
    private val iconInfo = view.findViewById<ImageView>(R.id.image_view_info)

    fun bind(task: Task) {
        description.text = task.text
        checkBox.isChecked = task.done

        setCheckBoxStyle(task)
        setIcons(task)
        setDescriptionStyle(task)
        setDate(task)

        checkBox.setOnClickListener {
            onCheckBoxClick(task, absoluteAdapterPosition)
        }
        iconInfo.setOnClickListener {
            (context as MainActivity).openEditNoteFragment(task)
        }
    }

    private fun setCheckBoxStyle(task: Task) {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked)
        )

        if (task.deadline != null && task.deadline < Calendar.getInstance().time) {
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

    private fun setIcons(task: Task) {
        if (task.priority == Priority.BASIC) {
            iconPriority.visibility = View.GONE
            return
        }
        iconPriority.visibility = View.VISIBLE
        if (task.priority == Priority.LOW)
            iconPriority.setImageResource(R.drawable.ic_low_priority)
        else
            iconPriority.setImageResource(R.drawable.ic_high_priority)
    }

    private fun setDescriptionStyle(task: Task) {
        if (task.done)
            description.paintFlags =
                description.paintFlags.or(Paint.STRIKE_THRU_TEXT_FLAG)
        else
            description.paintFlags =
                description.paintFlags.and(Paint.STRIKE_THRU_TEXT_FLAG.inv())
    }

    private fun setDate(task: Task) {
        if (task.deadline == null) {
            textViewDate.visibility = View.GONE
            return
        }
        val simpleDate = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        textViewDate.visibility = View.VISIBLE
        textViewDate.text = simpleDate.format(task.deadline)
    }
}