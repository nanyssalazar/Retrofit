package edu.iest.retrofit

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.iest.retrofit.adapters.DogsAdapter
import edu.iest.retrofit.models.API
import edu.iest.retrofit.models.ImageRandom
import edu.iest.retrofit.models.ImagesBreed
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var spinner : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // identificamos nuestro array

        // agregamos valores a nuestro spinner
        val arrayList = ArrayList<String>()
        arrayList.add("Samoyed")
        arrayList.add("Affenpinscher")
        arrayList.add("Briard")

        // creamos un arrayAdapter para el spinner
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)

        spinner = findViewById<Spinner>(R.id.spinner)
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
        spinner = findViewById<Spinner>(R.id.spinner)
        val raza = spinner.getSelectedItem().toString()

       // Log.d("RAZA",raza)

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
        val raza: String = parent?.getItemAtPosition(position).toString()
        Log.d("raza", raza)
        val apiCall = API().crearServicioAPI()

        val layout = findViewById<View>(R.id.imageLayout) as LinearLayout

        // obtenemos solamente imagenes de perros que sean del tipo de raza hound
        apiCall.listaImagenesDePerrosPorRaza(raza.lowercase()).enqueue(object : Callback<ImagesBreed>{
            override fun onResponse(call: Call<ImagesBreed>, response: Response<ImagesBreed>) {
                val dogs = response.body()?.message
                Log.d("PRUEBAS", "Status de la respuesta ${response.body()?.status}")
                if(dogs!=null) {
                    val linearLayoutManager = LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.HORIZONTAL, false
                    )

                    val recycler = findViewById<RecyclerView>(R.id.recyclerDogs)
                    recycler.layoutManager = linearLayoutManager

                    recycler.adapter = DogsAdapter(dogs,this@MainActivity)
                    for(dog in dogs) {
                       /* val image = ImageView(this@MainActivity)
                        Picasso.get().load(dog).into(image);
                        image.layoutParams = ViewGroup.LayoutParams(80, 60)
                        image.maxHeight = 500
                        image.maxWidth = 500

                        // Adds the view to the layout

                        // Adds the view to the layout
                        layout.addView(image)*/
                        Log.d("PRUEBAS", "Perro es $dog")
                    }
                }

            }

            override fun onFailure(call: Call<ImagesBreed>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}