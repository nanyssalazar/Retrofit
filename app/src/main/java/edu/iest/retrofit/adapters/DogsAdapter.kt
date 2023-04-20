package edu.iest.retrofit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.iest.retrofit.R
import edu.iest.retrofit.models.Perros

class DogsAdapter(dogs: List<String>?, contexto: Context) :
    RecyclerView.Adapter<DogsAdapter.ContenedorDeVista> () {
    // el tipo de dato de la lista que uso es String
    var innerDogs: List<String>? = dogs
    var innerContext: Context = contexto

    inner class ContenedorDeVista(view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val ivPerro : ImageView

        init {
            ivPerro = view.findViewById(R.id.ivPerro)
        }

        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContenedorDeVista {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_dog, parent, false)

        return ContenedorDeVista(view)
    }

    override fun onBindViewHolder(holder: ContenedorDeVista, position: Int) {
       val dog: String = innerDogs!!.get(position)
        Picasso.get().load(dog).into(holder.ivPerro)

        // Picasso.get().load(response.body()?.message).into(ivPicasso);

    }

    override fun getItemCount(): Int {
        return innerDogs!!.size
    }

}