package edu.iest.retrofit.models

import edu.iest.retrofit.network.ApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class API {
    private val URL_BASE = "https://dog.ceo/api/"

    // creamos nuestro servicio para consumir nuestra api
    fun crearServicioAPI(): ApiInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiInterface =
            retrofit.create(ApiInterface::class.java)
        return service }
}