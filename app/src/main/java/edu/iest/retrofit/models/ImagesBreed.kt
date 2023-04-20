package edu.iest.retrofit.models

// modelo de ImagesBreed contiene: status y message (es un List<String> porque eso es lo que devuelve nuestra llamada a la API)
data class ImagesBreed(
    val status: String,
    val message: List<String>
    )
