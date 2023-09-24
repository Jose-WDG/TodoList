package br.com.fiap.todolist.presentation.todolist.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.todolist.data.remote.FirebaseRepository
import br.com.fiap.todolist.databinding.TodolistItemBinding
import br.com.fiap.todolist.presentation.todolist.model.TodoListModel
import java.util.Collections

class TodoListAdapter(
    private val items: ArrayList<TodoListModel>,
    private val onLongClickListener: OnClickNote,
    private val moveNoTeCallback: MoveNoTeCallback
) :
    RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

    fun updateListItems(items: List<TodoListModel>) {
        val diffCallback = TodoListDiffCallback(this.items, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

//    fun updateItem(newItem: TodoListModel) {
//        this.items.add(newItem)
//        notifyItemChanged(items.size - 1, items.size)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        val binding =
            TodolistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoListViewHolder(binding, onLongClickListener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        FirebaseRepository().requestUpdateListPosition(items)?.addOnCompleteListener {
            if (!it.isSuccessful){
                moveNoTeCallback.onErrorMoveNotePosition {
                    onItemMove(fromPosition,toPosition)
                }
            }
        }
    }

    class TodoListViewHolder(
        private val binding: TodolistItemBinding,
        private val onLongClickListener: OnClickNote
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TodoListModel, position: Int) {
            binding.itemTitle.text = item.title
            binding.itemBody.text = item.textBody
            binding.cardBg.apply {
                background?.state =
                    intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
                setBackgroundColor(Color.parseColor(item.backGroundColor))
                setOnClickListener {
                    onLongClickListener.clickNote(item)
                }
            }
        }
    }

    class TodoListDiffCallback(
        private val oldList: List<TodoListModel>,
        private val newList: List<TodoListModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // Esta comparação pode variar dependendo da sua implementação
            // Se os seus itens têm um ID único, use-o para comparação
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    interface OnClickNote {
        fun clickNote(note: TodoListModel)


    }

    interface MoveNoTeCallback {
        fun onErrorMoveNotePosition(action: () -> Unit)
    }

    class DragAndDrop(private val adapter: TodoListAdapter) : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            adapter.onItemMove(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit
    }
}