package com.example.bddd_examen_repaso_xml.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    EmpleadoContract.DATABASE_NAME,
    null,
    EmpleadoContract.DATABASE_VERSION
) {


    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
           CREATE TABLE ${EmpleadoContract.TABLE_NAME} (
              ${EmpleadoContract.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
              ${EmpleadoContract.COLUMN_NOMBRE} TEXT NOT NULL,
              ${EmpleadoContract.COLUMN_SUELDO} REAL NOT NULL,
              ${EmpleadoContract.COLUMN_FECHA} TEXT NOT NULL
           )
       """.trimIndent()
        db?.execSQL(createTable)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${EmpleadoContract.TABLE_NAME}")
        onCreate(db)
    }


    // ========== CRUD ==========


    fun insert(empleado: Empleado): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(EmpleadoContract.COLUMN_NOMBRE, empleado.nombre)
            put(EmpleadoContract.COLUMN_SUELDO, empleado.sueldo)
            put(EmpleadoContract.COLUMN_FECHA, empleado.fechaContratacion)
        }
        val id = db.insert(EmpleadoContract.TABLE_NAME, null, values)
        db.close()
        return id
    }


    fun getAll(): List<Empleado> {
        val lista = mutableListOf<Empleado>()
        val db = readableDatabase
        val cursor = db.query(
            EmpleadoContract.TABLE_NAME,
            null, null, null, null, null,
            "${EmpleadoContract.COLUMN_ID} ASC"
        )


        with(cursor) {
            while (moveToNext()) {
                lista.add(Empleado(
                    id = getInt(getColumnIndexOrThrow(EmpleadoContract.COLUMN_ID)),
                    nombre = getString(getColumnIndexOrThrow(EmpleadoContract.COLUMN_NOMBRE)),
                    sueldo = getDouble(getColumnIndexOrThrow(EmpleadoContract.COLUMN_SUELDO)),
                    fechaContratacion = getString(getColumnIndexOrThrow(EmpleadoContract.COLUMN_FECHA))
                ))
            }
            close()
        }
        db.close()
        return lista
    }


    fun update(empleado: Empleado): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(EmpleadoContract.COLUMN_NOMBRE, empleado.nombre)
            put(EmpleadoContract.COLUMN_SUELDO, empleado.sueldo)
            put(EmpleadoContract.COLUMN_FECHA, empleado.fechaContratacion)
        }
        val rows = db.update(
            EmpleadoContract.TABLE_NAME,
            values,
            "${EmpleadoContract.COLUMN_ID} = ?",
            arrayOf(empleado.id.toString())
        )
        db.close()
        return rows
    }


    fun delete(id: Int): Int {
        val db = writableDatabase
        val rows = db.delete(
            EmpleadoContract.TABLE_NAME,
            "${EmpleadoContract.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return rows
    }

    fun deleteAll(): Int {
        val db = writableDatabase
        val rows = db.delete(EmpleadoContract.TABLE_NAME, null, null)
        db.close()
        return rows
    }

}
