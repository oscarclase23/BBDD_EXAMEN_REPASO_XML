package com.example.bddd_examen_repaso_xml


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bddd_examen_repaso_xml.databinding.ActivityMainBinding
import com.example.bddd_examen_repaso_xml.db.DatabaseHelper
import com.example.bddd_examen_repaso_xml.db.Empleado
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Instanciar DatabaseHelper
        dbHelper = DatabaseHelper(this)


        // Configurar botones
        setupButtons()
    }


    private fun setupButtons() {
        // Botón Guardar
        binding.btnGuardar.setOnClickListener {
            guardarEmpleado()
        }


        /// Botón Siguiente Pantalla (para ver lista de empleados)
        binding.btnPasarPantalla.setOnClickListener {
            // Ir a ListaActivity
            val intent = Intent(this, ListaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun guardarEmpleado() {
        // Obtener datos de los campos
        val nombre = binding.etName.text.toString().trim()
        val sueldoText = binding.etSueldo.text.toString().trim()


        // Validaciones
        if (nombre.isEmpty()) {
            Toast.makeText(this, "Ingresa un nombre", Toast.LENGTH_SHORT).show()
            return
        }


        if (sueldoText.isEmpty()) {
            Toast.makeText(this, "Ingresa un sueldo", Toast.LENGTH_SHORT).show()
            return
        }


        val sueldo = sueldoText.toDoubleOrNull()
        if (sueldo == null || sueldo <= 0) {
            Toast.makeText(this, "Sueldo inválido", Toast.LENGTH_SHORT).show()
            return
        }


        // Obtener fecha actual
        val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())


        // Crear objeto Empleado
        val empleado = Empleado(
            id = 0,  // SQLite lo autogenera
            nombre = nombre,
            sueldo = sueldo,
            fechaContratacion = fechaActual
        )


        // Insertar en BD
        val id = dbHelper.insert(empleado)


        if (id != -1L) {
            Toast.makeText(this, "Empleado guardado", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
        }
    }


    private fun mostrarEmpleados() {
        val empleados = dbHelper.getAll()


        if (empleados.isEmpty()) {
            Toast.makeText(this, "No hay empleados guardados", Toast.LENGTH_SHORT).show()
        } else {
            // Para el examen, mostrar en Toast (en producción usarías RecyclerView)
            val mensaje = empleados.joinToString("\n") {
                "${it.nombre} - $${it.sueldo}"
            }
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        }
    }


    private fun limpiarCampos() {
        binding.etName.text?.clear()
        binding.etSueldo.text?.clear()
        binding.etName.requestFocus()
    }
}
