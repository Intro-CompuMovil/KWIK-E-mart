package co.kwik_e_mart

import android.content.Context
import android.os.BugreportManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.processNextEventInCurrentThread
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.reflect.Type

class DataManager (private val context: Context) {
    //Metodo para inicializar el archivo de productoCompra.json si no existe
    fun initFile() {
        val file = File(context.filesDir, "productoCompra.json")
        if(!file.exists()) {
            file.createNewFile()
        }
    }

    // Metodo para cargar la lista de productos desde el archivo JSON
    fun cargarProductos(): List<Productos> {
        val gson = Gson()
        val destinosJson = cargarJSONDesdeArchivo()
        val tipoLista = object : TypeToken<List<Productos>>() {}.type
        return gson.fromJson(destinosJson, tipoLista)
    }

    //Metodo para cargar la lista de carritoCompra
    fun cargarListaCompra(): MutableList<Productos> {
        val gson = Gson()
        val carritoCompraJson = context.openFileInput("productoCompra.json")?.use {
            BufferedReader(InputStreamReader(it)).readText()
        } ?: ""
        try {
            val tipoLista = object : TypeToken<MutableList<Productos>>() {}.type
            return gson.fromJson(carritoCompraJson, tipoLista) ?: mutableListOf()
        } catch (e: JsonSyntaxException) {
            // Manejar la excepción aquí
            e.printStackTrace()
            return mutableListOf()
        }
    }

    fun guardarProductoCar(carrito: Productos) {
        //Obtener la lista actual de productos carrito
        val carritoCompra = cargarListaCompra()

        // Agregar el nuevo destino a la lista de favoritos si no esta presente
        if (!carritoCompra.contains(carrito)) {
            carritoCompra.add(carrito)
            //Guardar la lista actualizada
            guardarProductoCarrito(carritoCompra)
        }
    }

    fun guardarProductoCarrito(carrito: List<Productos>) {
        val gson = Gson()
        val carritoCompraJson = gson.toJson(carrito)
        context.openFileOutput("productoCompra.json", Context.MODE_PRIVATE).use {
            OutputStreamWriter(it).use { writer ->
                writer.write(carritoCompraJson)
            }
        }
    }

    fun cargarProductosSeleccionado(categoria: String): List<Productos> {
        val productos = cargarProductos()
        return productos.filter { it.categoria == categoria }
    }

    fun obtenerDetalles(id: Int): Productos? {
        val productos = cargarProductos()
        return productos.find  { it.id == id }
    }
    private fun cargarJSONDesdeArchivo(): String {
        val inputStream = context.resources.openRawResource(R.raw.productos)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        return stringBuilder.toString()
    }
}