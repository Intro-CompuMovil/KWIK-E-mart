package co.kwik_e_mart.Gerente

import androidx.lifecycle.ViewModel
import android.util.Log

class SharedListaCompraxDetallesDestino : ViewModel() {
    var nombreSeleccionado: String? = null
    init {
        Log.d("MyViewModel", "El valor del string es: $nombreSeleccionado")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("SharedListaCompraxDetallesDestino", "ViewModel destruido")
    }
}