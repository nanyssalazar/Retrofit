package edu.iest.retrofit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.iest.retrofit.R

/* la vista RecyclerView necesita de un Adaptador el cual a su vez depende de un
 Contenedor de Vistas (ViewHolder) y un conjunto de dato */

// le pasamos como tipo de dato un List<String> porque ese es el tipo de dato que mandamos en nuestra MainActivity (la API responde con una lista de links)
class DogsAdapter(dogs: List<String>?, contexto: Context) :
    /* haremos que nuestro adaptador heredé de la clase RecyclerView.Adapter usando como tipo de dato el contenedor recién creado
    (el adapter estara en rojo , es necesario implementar los métodos ( ctr + enter para importar) */
    RecyclerView.Adapter<DogsAdapter.ContenedorDeVista> () {
    // el tipo de dato de la lista que se usa es String
    var innerDogs: List<String>? = dogs
    var innerContext: Context = contexto

   /* esta inner class será nuestro contenedor para este ejemplo llamada ContenedorDeVista
    tendrá como parametro View y heredará de la clase RecyclerView.ViewHolder(view) */
    inner class ContenedorDeVista(view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener {
       // definimos que tipo de dato es nuestra variable que vamos a ir cambiando
        val ivPerro : ImageView

      /* dentro del init de nuestro contenedor asignaremos las diferentes vistas (View,TextView, Button,etc)
       así como eventos (setOnClickListener, setOnItemSelected,etc) pertenecientes al layout que deseamos
       repetir como lista “activity_dog.xml” */
        init {
          // es la imagen que deseamos que sea repetida en nuestro recycler
            ivPerro = view.findViewById(R.id.ivPerro)
        }

        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }
    }

    // onCreateViewHolder será el método donde inflemos nuestro layout (la que queremos usar en el recycler)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContenedorDeVista {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_dog, parent, false)
        return ContenedorDeVista(view)
    }

    /* será el encargado de ir repitiendo el diseño de nuestro layout según las veces definidas en getItemCount,
     podremos aplicar la lógica para manipular la información a mostrar para cada vista */
    override fun onBindViewHolder(holder: ContenedorDeVista, position: Int) {
        // en esta variable iremos guardando el link (string) de la lista cada que esta se va recorriendo
        val dog: String = innerDogs!!.get(position)
        // aqui usamos picasso para ir cargando cada link en nuestro ImageView
        // dog es nuestro link
        // holder.ivPerro nos permite definir en donde cargaremos ese link
        Picasso.get().load(dog).into(holder.ivPerro)
    }

    // getItemCount esperará un entero con el valor de cuantas filas (elementos) va a mosta
    override fun getItemCount(): Int {
        return innerDogs!!.size
    }

}