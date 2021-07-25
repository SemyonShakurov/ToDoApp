package com.sscorp.todo.tasks_list

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sscorp.todo.R
import com.sscorp.todo.activities.MainActivity

class LastItemViewHolder(context: Context, view: View) : RecyclerView.ViewHolder(view) {

    private val textViewNew = view.findViewById<TextView>(R.id.text_view_new)

    init {
        textViewNew.setOnClickListener {
            (context as MainActivity).openEditNoteFragment()
        }
    }
}