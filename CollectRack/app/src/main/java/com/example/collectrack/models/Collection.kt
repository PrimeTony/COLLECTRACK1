package com.example.collectrack.models

class Collection (
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val image: String = ""
) {
    override fun toString(): String {
        return "Collection(name='$name', description='$description', Category='$category', image='$image')"
    }
}