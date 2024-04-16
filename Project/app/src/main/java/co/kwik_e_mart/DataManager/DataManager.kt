package co.kwik_e_mart.DataManager

import android.content.Context
import android.util.Log
import co.kwik_e_mart.Entrega.Entregas
import co.kwik_e_mart.Mensajero.Mensajeros
import co.kwik_e_mart.Productos.Productos
import co.kwik_e_mart.R
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type


class DataManager(private val context: Context) {

    private val gson = Gson()
    private val productos: List<Productos>
    private val mensajeros: List<Mensajeros>
    private val entregas: List<Entregas>

    init {
        productos = cargarProductosJSON()
    }

    init {
        mensajeros = cargarProductosJSONMensajero()
        Log.d("Mensajeros Iniciados", "Tamaño de la lista de mensajeros cargados: ${mensajeros.size}")
    }

    init {
        entregas = cargarEntregasJSON()
    }

    fun init() {
        // Método init() para inicializar el DataManager
        cargarListaCompra()
    }


    fun obtenerDetalles(id: Int): Productos? {
        return productos.find { it.id == id }
    }

    fun guardarProductoCar(producto: Productos) {
        // Agregar el producto al carrito
        val carrito = cargarListaCompra().toMutableList()
        carrito.add(producto)
        // Guardar la lista de productos del carrito
        guardarProductoCarrito(carrito)
    }

    private fun cargarProductosJSON(): List<Productos> {
        val jsonString = leerJSONDesdeArchivo()
        val tipoLista: Type = object : TypeToken<List<Productos>>() {}.type
        return try {
            gson.fromJson(jsonString, tipoLista)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun leerJSONDesdeArchivo(): String {
        val inputStream = context.resources.openRawResource(R.raw.productos)
        return inputStream.bufferedReader().use { it.readText() }
    }

    private fun cargarProductosJSONMensajero(): List<Mensajeros> {
        val jsonString = leerJSONDesdeArchivoMensajero()
        val tipoLista: Type = object : TypeToken<List<Mensajeros>>() {}.type
        return try {
            gson.fromJson(jsonString, tipoLista)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun leerJSONDesdeArchivoMensajero(): String {
        val inputStream = context.resources.openRawResource(R.raw.mensajeros)
        return inputStream.bufferedReader().use { it.readText() }
    }

    private fun cargarEntregasJSON(): List<Entregas> {
        val jsonString = leerJSONDesdeArchivoEntregas()
        val tipoLista: Type = object : TypeToken<List<Entregas>>() {}.type
        return try {
            gson.fromJson(jsonString, tipoLista)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun leerJSONDesdeArchivoEntregas(): String {
        val inputStream = context.resources.openRawResource(R.raw.entregas)
        return inputStream.bufferedReader().use { it.readText() }
    }



    fun cargarListaCompra(): List<Productos> {
        val carritoCompraFile = File(context.filesDir, "productoCompra.json")

        // Si el archivo no existe, lo crea
        if (!carritoCompraFile.exists()) {
            try {
                carritoCompraFile.createNewFile()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val carritoCompraJson = try {
            carritoCompraFile.readText()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

        val tipoLista: Type = object : TypeToken<List<Productos>>() {}.type
        return try {
            gson.fromJson(carritoCompraJson, tipoLista) ?: mutableListOf()
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            mutableListOf()
        }
    }

    fun cargarListaMensajeros(): List<Mensajeros> {
        val mensajeroFile = File(context.filesDir, "mensajeros.json")

        // Si el archivo no existe, lo crea
        if (!mensajeroFile.exists()) {
            try {
                mensajeroFile.createNewFile()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val mensajeroJson = try {
            mensajeroFile.readText()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

        val tipoLista: Type = object : TypeToken<List<Mensajeros>>() {}.type
        return try {
            gson.fromJson(mensajeroJson, tipoLista) ?: mutableListOf()
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            mutableListOf()
        }
    }


    fun cargarListaEntregas(): List<Entregas> {
        val entregaFile = File(context.filesDir, "entregasProd.json")

        // Si el archivo no existe, lo crea
        if (!entregaFile.exists()) {
            try {
                entregaFile.createNewFile()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val entregaJson = try {
            entregaFile.readText()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

        val tipoLista: Type = object : TypeToken<List<Entregas>>() {}.type
        return try {
            gson.fromJson(entregaJson, tipoLista) ?: mutableListOf()
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            mutableListOf()
        }
    }

    fun guardarProductoCarrito(carrito: List<Productos>) {
        val carritoCompraJson = gson.toJson(carrito)
        context.openFileOutput("productoCompra.json", Context.MODE_PRIVATE).use {
            it.write(carritoCompraJson.toByteArray())
        }
    }

    fun cargarProductos(): List<Productos> {
        return productos
    }

    fun cargarMensjaeros(): List<Mensajeros> {

        return mensajeros
    }


    fun cargarProductosSeleccionado(categoria: String): List<Productos> {
        return productos.filter { it.categoria == categoria }
    }
}