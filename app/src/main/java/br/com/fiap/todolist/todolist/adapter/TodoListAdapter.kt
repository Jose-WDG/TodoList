package br.com.fiap.todolist.todolist.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.todolist.databinding.TodolistItemBinding
import br.com.fiap.todolist.todolist.model.TodoListModel

class TodoListAdapter(private val items: ArrayList<TodoListModel>) :
    RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

    fun updateListItems(items: List<TodoListModel>) {
        val diffCallback = TodoListDiffCallback(this.items, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateItem(newitem: TodoListModel){
        this.items.add(newitem)
        notifyItemChanged(items.size -1,items.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        val binding =
            TodolistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoListViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    class TodoListViewHolder(private val binding: TodolistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TodoListModel, position: Int) {
            binding.itemTitle.text = item.title
            binding.itemBody.text = item.textBody
            binding.cardBg.setBackgroundColor(Color.parseColor(item.backGroundColor))
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
}