package co.kwik_e_mart.DataManager

import android.content.Context
import co.kwik_e_mart.Gerente.Productos
import co.kwik_e_mart.R
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type


class DataManager(private val context: Context) {

    private val gson = Gson()
    private val productos: List<Productos>

    init {
        productos = cargarProductosJSON()
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