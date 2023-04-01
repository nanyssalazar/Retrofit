package edu.iest.retrofit

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import edu.iest.retrofit.models.API
import edu.iest.retrofit.models.ImageRandom
import edu.iest.retrofit.models.ImagesBreed
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiCall = API().crearServicioAPI()
        val ivPicasso = findViewById<ImageView>(R.id.ivPicasso)

        apiCall.imagenAleatoria().enqueue(object : Callback<ImageRandom> {
            override fun onResponse(
                call: Call<ImageRandom>, response:
                Response<ImageRandom>) {
                // cargamos la imagen random aqui con el response.bodu()?.message
                    Picasso.get().load(response.body()?.message).into(ivPicasso);

                    Log.d("API_PRUEBA", "status es " + response.body()?.status)
                    Log.d("API_PRUEBA ", "message es " + response.body()?.message)
                // Aqui hacer lo que necesitemos con los datos }
            }
                override fun onFailure(call: Call<ImageRandom>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "No fue posible conectar a API",
                        Toast.LENGTH_SHORT
                    ).show()
                }




        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_menu_list_images) {
            Toast.makeText(
                this, "OPTION menu 1",
                Toast.LENGTH_SHORT).show()

            val apiCall = API().crearServicioAPI()
            apiCall.listaImagenesDePerrosPorRaza("hound").enqueue(object : Callback<ImagesBreed>{
                override fun onResponse(call: Call<ImagesBreed>, response: Response<ImagesBreed>) {
                    val dogs = response.body()?.message
                    Log.d("PRUEBAS", "Status de la respuesta ${response.body()?.status}")
                    if(dogs!=null) {
                        for(dog in dogs) {
                            Log.d("PRUEBAS", "Perro es $dog")

                            
                        }
                    }

                }

                override fun onFailure(call: Call<ImagesBreed>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_images, menu)
        return super.onCreateOptionsMenu(menu)
    }

}