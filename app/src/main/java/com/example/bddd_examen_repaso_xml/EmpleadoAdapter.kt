package com.example.bddd_examen_repaso_xml

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bddd_examen_repaso_xml.databinding.ItemEmpleadoBinding
import com.example.bddd_examen_repaso_xml.db.Empleado




class EmpleadoAdapter(
    private var empleados: List<Empleado>
) : RecyclerView.Adapter<EmpleadoAdapter.EmpleadoViewHolder>() {


    // ViewHolder con ViewBinding
    inner class EmpleadoViewHolder(private val binding: ItemEmpleadoBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(empleado: Empleado) {
            binding.tvNombreCard.text = empleado.nombre
            binding.tvSueldoCard.text = "${empleado.sueldo}€"
            // Click en el icono de eliminar
            binding.ivTrash.setOnClickListener {
                onDeleteClick?.invoke(empleado)
            }
        }
    }

    // Variable para el callback
    private var onDeleteClick: ((Empleado) -> Unit)? = null


    // Método para establecer el listener
    fun setOnDeleteClickListener(listener: (Empleado) -> Unit) {
        onDeleteClick = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpleadoViewHolder {
        val binding = ItemEmpleadoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EmpleadoViewHolder(binding)
    }


    override fun onBindViewHolder(holder: EmpleadoViewHolder, position: Int) {
        holder.bind(empleados[position])
    }


    override fun getItemCount(): Int = empleados.size


    // Método para actualizar la lista
    fun updateList(newList: List<Empleado>) {
        empleados = newList
        notifyDataSetChanged()
    }
}
