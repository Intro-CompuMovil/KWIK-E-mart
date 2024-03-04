package co.kwik_e_mart

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type

class DataManager(private val context: Context) {

    private val gson = Gson()
    private val productos: List<Productos>

    init {
        productos = cargarProductosJSON()
    }

    fun init() {
        // Método init() para inicializar el DataManager
        cargarProductosJSON()
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

    fun cargarListaCompra(): List<Productos> {
        val carritoCompraJson = context.openFileInput("productoCompra.json")?.use {
            BufferedReader(InputStreamReader(it)).readText()
        } ?: ""
        val tipoLista: Type = object : TypeToken<List<Productos>>() {}.type
        return try {
            gson.fromJson(carritoCompraJson, tipoLista) ?: mutableListOf()
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

    fun cargarProductosSeleccionado(categoria: String): List<Productos> {
        return productos.filter { it.categoria == categoria }
    }
}