// esta interfaz se crea en una carpeta llamada network
package edu.iest.retrofit.network

// importamos los modelos que utilizamos
import edu.iest.retrofit.models.ImageRandom
import edu.iest.retrofit.models.ImagesBreed

// importamos las funciones de retrofit2
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// interfaz de nuestra API con metodos
interface ApiInterface {
    // metodo para jalar una imagen random
    @GET("breeds/image/random")
    fun imagenAleatoria(): Call<ImageRandom>

    // metodo para conseguir una lista de imagenes de perros por raza
    @GET("breed/{raza}/images")
    fun listaImagenesDePerrosPorRaza
                (@Path("raza") raza: String) : Call<ImagesBreed>

}