package com.example.hetwapenapp

import android.os.Parcel
import android.os.Parcelable

data class Drinks(
    val id: Int,
    val name: String,
    val description: String,
    val type: String,
    val abv: Float,
    val stars: Int,
    val image: String,
    val company: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(type)
        parcel.writeFloat(abv)
        parcel.writeInt(stars)
        parcel.writeString(image)
        parcel.writeString(company)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Drinks> {
        override fun createFromParcel(parcel: Parcel): Drinks {
            return Drinks(parcel)
        }

        override fun newArray(size: Int): Array<Drinks?> {
            return arrayOfNulls(size)
        }
    }
}
