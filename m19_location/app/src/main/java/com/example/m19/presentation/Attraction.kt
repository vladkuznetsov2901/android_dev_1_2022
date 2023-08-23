package com.example.m19.presentation

import android.os.Parcel
import android.os.Parcelable

data class Attraction(
    val name: String?,
    val latitude: Double,
    val longitude: Double,
    val title: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Attraction> {
        override fun createFromParcel(parcel: Parcel): Attraction {
            return Attraction(parcel)
        }

        override fun newArray(size: Int): Array<Attraction?> {
            return arrayOfNulls(size)
        }
    }
}
