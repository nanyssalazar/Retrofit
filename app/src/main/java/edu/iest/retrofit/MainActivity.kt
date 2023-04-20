package edu.iest.retrofit

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
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
    // variables globales
    private lateinit var spinner : Spinner
    private lateinit var tvUrl : TextView
    private val URL_KEY = "url"

    private var url : String = ""
    private var urlPreferences : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("PREFERENCIAS", savedInstanceState?.getString(URL_KEY).toString())

        // identicamops el TextView que contendrá el link de la imagen random
        tvUrl = findViewById(R.id.tvUrl)
        // identificamos nuestro spinner
        spinner = findViewById(R.id.spinner)

        // identificamos en que ImageView se va a cargar nuestra imagen random
        val ivPicasso = findViewById<ImageView>(R.id.ivPicasso)


        // agregamos valores a nuestro spinner con un ArrayList
        val arrayList = ArrayList<String>()
        arrayList.add("Samoyed")
        arrayList.add("Affenpinscher")
        arrayList.add("Briard")

        // creamos un arrayAdapter para el spinner y agregamos nuestros valores del array
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)

        // agregamos el adapter de nuestro array a nuestro spinner
        spinner.setAdapter(arrayAdapter);

        // agregamos esta linea para poder implementar metodo de onItemSelected en spinner
        spinner.onItemSelectedListener = this

        // creamos nuestra llamada a la API
        val apiCall = API().crearServicioAPI()


        // llamamos nuestra API con el metodo de imagenAleatoria (ImageRandom) definido en API Interface
        apiCall.imagenAleatoria().enqueue(object : Callback<ImageRandom> {
            @SuppressLint("CommitPrefEdits")
            override fun onResponse(
                call: Call<ImageRandom>, response:
                Response<ImageRandom>) {
                    // guardamos la respuesta de la API en la variable url
                    url = response.body()?.message!!

                    // cargamos la imagen random aqui con el response.bodu()?.message
                    Picasso.get().load(url).into(ivPicasso)
                    // cambiamos el texto de nuestro TextView para que contenga el url de nuestra API
                    tvUrl.text = url

                    // guardamos en las preferencias la imagen
                    val sharedPreferences  = getSharedPreferences("PERSISTENCIA", MODE_PRIVATE)
                    val preferencesEditor = sharedPreferences.edit()
                    preferencesEditor.putString(URL_KEY,url)
                    preferencesEditor.apply()

                    Log.d("API_PRUEBA", "status es " + response.body()?.status)
                    Log.d("API_PRUEBA ", "message es " + url)

            }
                // metodo por si falla la llamada a la API
                override fun onFailure(call: Call<ImageRandom>, t: Throwable) {
                    // llamamos nuestras preferencias
                    val sharedPreferences  = getSharedPreferences("PERSISTENCIA", MODE_PRIVATE)
                    // obtenemos el link guardado anteriormente
                    urlPreferences = sharedPreferences.getString(URL_KEY,"").toString()
                    Log.d("urlpref", urlPreferences)

                    // cargamos el link de preferencias en nuestro ImageView
                    Picasso.get().load(urlPreferences).into(ivPicasso)
                    Toast.makeText(
                        this@MainActivity,
                        "La ultima imagen vista fue $urlPreferences",
                        Toast.LENGTH_LONG
                    ).show()

                }
        })
    }

    // metodo para que al dar click en nuestro icono de la aplicacion llame al metodo de nuestra API listaImagenesDePerrosPorRaza
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        spinner = findViewById<Spinner>(R.id.spinner)

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
                    // guardamos la respuesta de nuestra API (lista de strings con los links)
                    val dogs = response.body()?.message
                    Log.d("PRUEBAS", "Status de la respuesta ${response.body()?.status}")
                    if(dogs!=null) {
                        // ciclo pora que imprima cada link de manera individual
                        for(dog in dogs) {
                            Log.d("PRUEBAS", "Perro es $dog")
                        }
                    }
                }
                override fun onFailure(call: Call<ImagesBreed>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "No fue posible conectar a API",
                        Toast.LENGTH_SHORT
                    ).show()
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

    // metodo para trabajar cuando se seleccione algun valor del Spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // obtenemos cual fue el item seleccionado y lo guardamos en la variable raza de tipo String
        val raza: String = parent?.getItemAtPosition(position).toString()
        Log.d("raza", raza)
        // creamos servicios de API
        val apiCall = API().crearServicioAPI()

        // obtenemos solamente imagenes de perros que sean del tipo de raza especificada en el spinner (usamos lowercase para poder llamar a la API)
        apiCall.listaImagenesDePerrosPorRaza(raza.lowercase()).enqueue(object : Callback<ImagesBreed>{
            override fun onResponse(call: Call<ImagesBreed>, response: Response<ImagesBreed>) {
                // guardamos la lista de links en la variable dogs
                val dogs = response.body()?.message
                Log.d("PRUEBAS", "Status de la respuesta ${response.body()?.status}")

                // si la respuesta no fue null entonces
                if(dogs!=null) {
                    // usamos un linearLayout para guardar nuestras imagenes
                    val linearLayoutManager = LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.HORIZONTAL, false
                    )
                    // idenficamos nuestro recycler en la vista
                    val recycler = findViewById<RecyclerView>(R.id.recyclerDogs)

                    // le agregamos un LinearLayout al recycler
                    recycler.layoutManager = linearLayoutManager

                    // usamos como adaptador para el recycler nuestra clase DogsAdapter y le pasamos los links guardados en la variable Dogs y el contexto
                    recycler.adapter = DogsAdapter(dogs,this@MainActivity)
                }
            }
            override fun onFailure(call: Call<ImagesBreed>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "No fue posible conectar a API",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("PREFERENCIAS ON SAVE", "onSaveInstanceState")
        outState.putString(URL_KEY, urlPreferences)
        outState?.run {
            putString(URL_KEY, urlPreferences)
        }
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        if(TextUtils.isEmpty(urlPreferences)){
            val sharedPreferences  = getSharedPreferences("PERSISTENCIA", MODE_PRIVATE)
            urlPreferences = sharedPreferences.getString(URL_KEY,"").toString()
        }
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
