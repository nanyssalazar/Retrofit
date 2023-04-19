package edu.iest.retrofit

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import edu.iest.retrofit.models.API
import edu.iest.retrofit.models.ImageRandom
import edu.iest.retrofit.models.ImagesBreed
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // identificamos nuestro array
        val spinner = findViewById<Spinner>(R.id.spinner)

        // agregamos valores a nuestro spinner
        val arrayList = ArrayList<String>()
        arrayList.add("Hound")
        arrayList.add("Tipo 2")
        arrayList.add("Tipo 3")

        // creamos un arrayAdapter para el spinner
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)

        // agregamos adapter a nuestro spinner
        spinner.setAdapter(arrayAdapter);
        spinner.onItemSelectedListener = this

        // aqui creamos nuestra llamada a la API
        val apiCall = API().crearServicioAPI()
        val ivPicasso = findViewById<ImageView>(R.id.ivPicasso)



        // llamamos nuestra API con el metodo de imagenAleatoria (ImageRandom)
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

    // metodo para que al dar click en nuestro icono de la aplicacion llame al metodo de nuestra API listaImagenesDePerrosPorRaza
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // indicamos que es nuestro icono con su id
        if (item.itemId == R.id.option_menu_list_images) {
            Toast.makeText(
                this, "OPTION menu 1",
                Toast.LENGTH_SHORT).show()

            // creamos servicios de API
            val apiCall = API().crearServicioAPI()
            // obtenemos solamente imagenes de perros que sean del tipo de raza hound
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

    // preguntar para que es esto
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_images, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text: String = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}