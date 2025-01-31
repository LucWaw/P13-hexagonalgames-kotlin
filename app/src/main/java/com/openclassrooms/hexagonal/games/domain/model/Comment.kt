package com.openclassrooms.hexagonal.games.domain.model

import java.io.Serializable

data class Comment(
    val id: String = "",
    val content: String = "",
    val timestamp: Long = 0,
    val author: User? = null
) : Serializable