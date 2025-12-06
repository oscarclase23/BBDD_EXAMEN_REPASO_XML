package com.example.bddd_examen_repaso_xml

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bddd_examen_repaso_xml.databinding.ActivityListaBinding
import com.example.bddd_examen_repaso_xml.db.DatabaseHelper
import com.example.bddd_examen_repaso_xml.db.Empleado


class ListaActivity : AppCompatActivity() {


    private lateinit var binding: ActivityListaBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: EmpleadoAdapter
    private var empleadosCompletos = listOf<Empleado>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        dbHelper = DatabaseHelper(this)


        setupRecyclerView()
        cargarEmpleados()
        setupSpinner()
    }


    private fun setupRecyclerView() {
        adapter = EmpleadoAdapter(emptyList())
        adapter.setOnDeleteClickListener { empleado ->
            eliminarEmpleado(empleado)
        }
        binding.recyclerViewEmpleados.apply {
            layoutManager = LinearLayoutManager(this@ListaActivity)
            adapter = this@ListaActivity.adapter
        }
    }

    private fun eliminarEmpleado(empleado: Empleado) {
        dbHelper.delete(empleado.id)
        Toast.makeText(this, "Empleado eliminado", Toast.LENGTH_SHORT).show()
        cargarEmpleados() // Recargar lista
    }


    private fun cargarEmpleados() {
        // ✅ CORRECCIÓN: Guardar en la variable de clase
        empleadosCompletos = dbHelper.getAll()
        adapter.updateList(empleadosCompletos)
    }


    private fun setupSpinner() {
        val opciones = listOf(
            "Ordenar por nombre (A-Z)",
            "Ordenar por sueldo (Mayor a Menor)",
            "Ordenar por sueldo (Menor a Mayor)"
        )


        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            opciones
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFiltro.adapter = spinnerAdapter


        binding.spinnerFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ordenarEmpleados(position)
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    private fun ordenarEmpleados(opcion: Int) {
        val listaOrdenada = when (opcion) {
            0 -> empleadosCompletos.sortedBy { it.nombre }
            1 -> empleadosCompletos.sortedByDescending { it.sueldo }
            2 -> empleadosCompletos.sortedBy { it.sueldo }
            else -> empleadosCompletos
        }
        adapter.updateList(listaOrdenada)
    }
}


