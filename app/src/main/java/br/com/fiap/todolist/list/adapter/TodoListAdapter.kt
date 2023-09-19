package br.com.fiap.todolist.list.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.todolist.databinding.TodolistItemBinding
import br.com.fiap.todolist.list.model.TodoListModel

class TodoListAdapter(private val items: List<TodoListModel>) : RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        val binding = TodolistItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return TodoListViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    class TodoListViewHolder(private val binding: TodolistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TodoListModel, position: Int) {
            binding.itemTitle.text = item.title
            binding.itemBody.text = item.textBody
            binding.cardBg.setBackgroundColor(Color.parseColor(item.backGroundColor))
        }
    }
}