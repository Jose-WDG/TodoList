package br.com.fiap.todolist.presentation.utils

import android.view.View

fun View.makeInVisible(makeVisible: Boolean){
    visibility = if (makeVisible){
        View.INVISIBLE
    }else{
        View.VISIBLE
    }
}

fun View.makeVisible(makeVisible: Boolean){
    visibility = if (makeVisible){
        View.VISIBLE
    }else{
        View.GONE
    }
}