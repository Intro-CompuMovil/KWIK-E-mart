package co.kwik_e_mart.Paquetes

import android.os.Parcel
import android.os.Parcelable

data class Paquetes(
    val id: Int,
    val producto1: String?,
    val producto2: String?,
    val producto3: String?,
    val producto4: String?,
    val producto5: String?,
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(producto1)
        parcel.writeString(producto2)
        parcel.writeString(producto3)
        parcel.writeString(producto4)
        parcel.writeString(producto5)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Paquetes> {
        override fun createFromParcel(parcel: Parcel): Paquetes {
            return Paquetes(parcel)
        }

        override fun newArray(size: Int): Array<Paquetes?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Paquetes

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}