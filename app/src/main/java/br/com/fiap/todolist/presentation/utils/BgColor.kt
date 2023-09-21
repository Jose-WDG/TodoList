package br.com.fiap.todolist.presentation.utils

import kotlin.random.Random

object BgColor {
    fun getRandomPostItColor(): String {
        val postItColors = listOf(
            "#FFEB3B", // Amarelo
            "#FFC107", // Laranja
            "#FF5722", // Vermelho-alaranjado
            "#4CAF50", // Verde
            "#03A9F4", // Azul
            "#9C27B0", // Roxo
            "#E91E63", // Rosa
            "#8BC34A", // Verde claro
            "#FF9800"  // Laranja claro
        )

        val randomIndex = Random.nextInt(postItColors.size)
        return postItColors[randomIndex]
    }
}