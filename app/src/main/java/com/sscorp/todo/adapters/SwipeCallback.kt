package com.sscorp.todo.adapters

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.sscorp.todo.R

class SwipeCallback(
    private val adapter: NotesAdapter
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {

    private val iconCheck =
        ContextCompat.getDrawable(adapter.context, R.drawable.ic_check)!!
    private val iconDelete =
        ContextCompat.getDrawable(adapter.context, R.drawable.ic_delete)!!

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition
        if (direction == ItemTouchHelper.RIGHT)
            adapter.checkItem(position)
        else
            adapter.deleteItem(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView

        when {
            dX > 0 -> {
                val background =
                    ColorDrawable(adapter.context.resources.getColor(R.color.green, null))

                val iconMargin = (itemView.height - iconCheck.intrinsicHeight) / 2
                val iconTop = itemView.top + (itemView.height - iconCheck.intrinsicHeight) / 2
                val iconLeft = itemView.left + iconMargin
                val iconRight = itemView.left + iconMargin + iconCheck.intrinsicWidth
                val iconBottom = iconTop + iconCheck.intrinsicHeight

                iconCheck.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                background.setBounds(
                    itemView.left, itemView.top,
                    itemView.left + dX.toInt(),
                    itemView.bottom
                )

                background.draw(c)
                iconCheck.draw(c)
            }
            else -> {
                val background =
                    ColorDrawable(adapter.context.resources.getColor(R.color.red, null))

                val iconMargin = (itemView.height - iconDelete.intrinsicHeight) / 2
                val iconTop = itemView.top + (itemView.height - iconDelete.intrinsicHeight) / 2
                val iconLeft = itemView.right - iconMargin - iconDelete.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                val iconBottom = iconTop + iconDelete.intrinsicHeight

                iconDelete.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top, itemView.right, itemView.bottom
                )

                background.draw(c)
                iconDelete.draw(c)
            }
        }
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder is LastItemViewHolder) return 0
        return super.getSwipeDirs(recyclerView, viewHolder)
    }
}