package co.kwik_e_mart.Entrega

import android.os.Parcel
import android.os.Parcelable

data class Entregas(
    val id: Int,
    val productos: String?,
    val direccion: String?,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(productos)
        parcel.writeString(direccion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Entregas> {
        override fun createFromParcel(parcel: Parcel): Entregas {
            return Entregas(parcel)
        }

        override fun newArray(size: Int): Array<Entregas?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Entregas

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
