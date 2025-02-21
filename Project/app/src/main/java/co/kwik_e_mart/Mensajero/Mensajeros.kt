package co.kwik_e_mart.Mensajero

import android.os.Parcel
import android.os.Parcelable

data class Mensajeros(
    val id: Int,
    val nombre: String?,
    val calificacion: Double,
    val preciopromedio: Double,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeDouble(calificacion)
        parcel.writeDouble(preciopromedio)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mensajeros> {
        override fun createFromParcel(parcel: Parcel): Mensajeros {
            return Mensajeros(parcel)
        }

        override fun newArray(size: Int): Array<Mensajeros?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Mensajeros

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
